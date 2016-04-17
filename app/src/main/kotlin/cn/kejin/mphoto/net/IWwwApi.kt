package cn.kejin.mphoto.net

import cn.kejin.mphoto.net.entities.PhotoDetail
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.net.okhttp.HttpCallback
import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
interface IWwwApi {
    enum class SortType {
        LATEST, POPULAR, OLDEST
    }

    fun getAbsUrl(uri: String): String
            = PhotoNet.getWWWAbsUrl(uri)

    fun getSortUrl(uri: String, sort: SortType): String {
        var url = getAbsUrl(uri)
        when(sort) {
            SortType.POPULAR -> {
                url = "$url&order_by=popular"
            }
            SortType.OLDEST -> {
                url = "$url&order_by=oldest"
            }
        }

        return url
    }

    fun getPhotoPage(page: Int, sort: SortType, callback: HttpCallback<PhotoPage>) : Call?

    fun getUserPhotoPage(name: String, page: Int, sort: SortType, callback: HttpCallback<PhotoPage>) : Call?

    fun getPhotoDetail(uri: String, callback: HttpCallback<PhotoDetail>)
}