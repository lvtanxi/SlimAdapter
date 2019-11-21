package com.slim.adapter.demo.helper



/**
 * Date: 2018-11-26
 * Time: 10:24
 * Description:
 */
class PageDelegate {
    private var pageNo =1
    private var pageSize =20
    var lastDataSize = -1


    fun resetPageNo() {
        this.lastDataSize=-1
        this.pageNo = 1
    }



    fun pageNoIncrement() {
        pageNo++
    }


    fun checkLoadEnd(loadSuccess: Boolean): Boolean {
        val loadEnd = loadSuccess && (lastDataSize < pageSize)
        if (!loadEnd)
            pageNo += 1
        return loadEnd
    }



    fun getPagingParam(pageNoKey: String = "pageNo", pageSizeKey: String = "pageSize") = mapOf(pageNoKey to pageNo.toString(), pageSizeKey to pageSize.toString())

    fun isFirstPage(): Boolean {
        return 1 == pageNo
    }

}

