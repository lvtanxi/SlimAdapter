package com.slim.adapter

import android.util.SparseIntArray

/**
 * Date: 2018-04-18
 * Time: 13:26
 * Description: 分组
 */
class SlimSectionAdapter: SlimAdapter() {
    //存储Sectioned对应的Position
    private val headerLocationMap = SparseIntArray()
    //存储Position对应的Sectioned
    private val sectionMap = SparseIntArray()
    //存储Position对应的子View的Position
    private val positionMap = SparseIntArray()

    override fun getItemCount(): Int {
        headerLocationMap.clear()
        sectionMap.clear()
        positionMap.clear()
        var count = 0
        list.withIndex().forEach {
            val itemCount = getSectionedCount(it.index)
                //这里先存储条目位置
            headerLocationMap.put(count, it.index)
            count += itemCount + 1
        }
        return count
    }

    private fun getSectionedCount(index: Int): Int {
        if(list[index] is Section)
            return (list[index] as Section).childCount()
        return 0
    }

    /**
     * 根据position获取条目位置
     */
    private fun getSectionIndex(itemPosition: Int): Int {
        //判断是否已经计算过了，避免重读计算
        if (sectionMap.get(itemPosition, -1) == -1 || positionMap.get(itemPosition, -1) == -1) {
            //枷锁计算
            synchronized(headerLocationMap) {
                var lastSectionIndex: Int? = -1
                val count = headerLocationMap.size()
                for (i in 0 until count) {
                    //得到条目
                    if (itemPosition >= headerLocationMap.keyAt(i)) {
                        lastSectionIndex = headerLocationMap.keyAt(i)
                    } else {
                        break
                    }
                }
                //存储次itemPosition的条目位置和itemPosition子view的位置
                sectionMap.put(itemPosition, headerLocationMap.get(lastSectionIndex!!))
                positionMap.put(itemPosition, itemPosition - lastSectionIndex - 1)
            }
        }
        return sectionMap.get(itemPosition,0)

    }

    override fun getItem(position: Int): Any {
        if (position==-1)
            println(">>>>>>>>>>>")
        val section = getSectionIndex(position)
        if (isSectioned(position)) {
            return list[section]
        } else if (list[section] is Section) {
            val child = positionMap.get(position,0)
            return (list[section] as Section).childItem(child)
        }
        throw RuntimeException("没有找到对应的item")
    }

    private fun isSectioned(position: Int): Boolean {
        return headerLocationMap.get(position, -1) != -1
    }
}