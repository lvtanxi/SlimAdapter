package com.slim.adapter.demo.helper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Region
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.slim.adapter.SlimAdapter

/**
 * Date: 2017-11-09
 * Time: 10:46
 * Description:悬浮分组条目
 */
class SectionDecoration(@LayoutRes private val  sectionLayoutId: Int) : RecyclerView.ItemDecoration() {
    private var mHeaderPosition: Int = 0
    private var mPinnedHeaderTop: Int = 0
    private var mVisiblePosition: Int = 0
    private var mIsAdapterDataChanged: Boolean = false

    private var mClipBounds: Rect? = null
    private var mPinnedHeaderView: View? = null
    private var mAdapter: SlimAdapter? = null

    private val mTypePinnedHeaderFactories = SparseArray<PinnedHeaderCreator>()
    //简体数据变化
    private val mAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            mIsAdapterDataChanged = true
        }
    }

    init {
        this.mHeaderPosition = -1
        this.mVisiblePosition = -1
        registerTypePinnedHeader(sectionLayoutId)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        createPinnedHeader(parent)

        if (mPinnedHeaderView != null) {
            val headerEndAt = mPinnedHeaderView!!.top + mPinnedHeaderView!!.height
            val v = parent.findChildViewUnder((c.width / 2).toFloat(), (headerEndAt + 1).toFloat())

            mPinnedHeaderTop = if (isPinnedView(parent, v)) {
                v.top - mPinnedHeaderView!!.height
            } else {
                0
            }

            mClipBounds = c.clipBounds
            mClipBounds!!.top = mPinnedHeaderTop + mPinnedHeaderView!!.height
            c.clipRect(mClipBounds!!)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (mPinnedHeaderView != null) {
            c.save()

            mClipBounds!!.top = 0
            c.clipRect(mClipBounds!!, Region.Op.UNION)
            c.translate(0f, mPinnedHeaderTop.toFloat())
            mPinnedHeaderView!!.draw(c)

            c.restore()
        }
    }

    private fun createPinnedHeader(parent: RecyclerView) {
        updatePinnedHeader(parent)
        val layoutManager = parent.layoutManager
        if (layoutManager == null || layoutManager.childCount <= 0) {
            return
        }
        val firstVisiblePosition = (layoutManager.getChildAt(0).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val headerPosition = findPinnedHeaderPosition(parent, firstVisiblePosition)
        mVisiblePosition = firstVisiblePosition
        if (headerPosition >= 0 && mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition
            val pinnedViewHolder = mAdapter!!.createViewHolder(parent, sectionLayoutId)
            if (mHeaderPosition==-1||mHeaderPosition==12){
                println(">>>>>>>")
            }
            mAdapter?.bindViewHolder(pinnedViewHolder,mHeaderPosition)
            mPinnedHeaderView = pinnedViewHolder.itemView

            // read layout parameters
            var layoutParams: ViewGroup.LayoutParams? = mPinnedHeaderView!!.layoutParams
            if (layoutParams == null) {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                mPinnedHeaderView!!.layoutParams = layoutParams
            }

            var heightMode = View.MeasureSpec.getMode(layoutParams.height)
            var heightSize = View.MeasureSpec.getSize(layoutParams.height)

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY
            }

            val maxHeight = parent.height - parent.paddingTop - parent.paddingBottom
            if (heightSize > maxHeight) {
                heightSize = maxHeight
            }

            // measure & layout
            val ws = View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft - parent.paddingRight, View.MeasureSpec.EXACTLY)
            val hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode)
            mPinnedHeaderView!!.setBackgroundColor(Color.GREEN)
            mPinnedHeaderView!!.measure(ws, hs)
            mPinnedHeaderView!!.layout(0, 0, mPinnedHeaderView!!.measuredWidth, mPinnedHeaderView!!.measuredHeight)
        }
    }

    private fun findPinnedHeaderPosition(parent: RecyclerView, fromPosition: Int): Int {
        if (fromPosition > mAdapter!!.itemCount || fromPosition < 0) {
            return -1
        }

        for (position in fromPosition downTo 0) {
            val viewType = mAdapter!!.getItemViewType(position)
            if (isPinnedViewType(parent, position, viewType)) {
                return position
            }
        }

        return -1
    }

    private fun isPinnedViewType(parent: RecyclerView, adapterPosition: Int, viewType: Int): Boolean {
        val pinnedHeaderCreator = mTypePinnedHeaderFactories.get(viewType)

        return pinnedHeaderCreator != null && pinnedHeaderCreator.create(parent, adapterPosition)
    }

    private fun isPinnedView(parent: RecyclerView, v: View): Boolean {
        val position = parent.getChildAdapterPosition(v)
        return if (position == RecyclerView.NO_POSITION) false else isPinnedViewType(parent, position, mAdapter!!.getItemViewType(position))

    }

    private fun updatePinnedHeader(parent: RecyclerView) {
        val adapter = parent.adapter
        if (mAdapter !== adapter || mIsAdapterDataChanged || mVisiblePosition < mHeaderPosition) {
            resetPinnedHeader()
            if (mAdapter != null) {
                mAdapter!!.unregisterAdapterDataObserver(mAdapterDataObserver)
            }

            mAdapter = adapter as SlimAdapter?
            if (mAdapter != null) {
                mAdapter!!.registerAdapterDataObserver(mAdapterDataObserver)
            }
        }
    }

    private fun resetPinnedHeader() {
        mHeaderPosition = -1
        mVisiblePosition = -1
        mPinnedHeaderView = null
    }

    private fun registerTypePinnedHeader(itemType: Int) {
        mTypePinnedHeaderFactories.put(itemType, object : PinnedHeaderCreator {
            override fun create(parent: RecyclerView, adapterPosition: Int): Boolean {
                return true
            }
        })
    }

    interface PinnedHeaderCreator {
        fun create(parent: RecyclerView, adapterPosition: Int): Boolean
    }
}