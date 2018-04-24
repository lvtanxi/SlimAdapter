package com.slim.adapter.demo.helper

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.slim.adapter.SlimConvert

/**
 * Date: 2018-04-24
 * Time: 10:31
 * Description:
 */
class ImageConvert(private val url: String?) : SlimConvert.Convert<ImageView> {
    override fun convert(v: ImageView) {
        Glide.with(v.context).load(url).into(v)
    }
}