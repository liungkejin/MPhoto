package cn.kejin.mphoto.net

import cn.kejin.mphoto.net.entities.PhotoDetail
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.net.okhttp.HttpCallback

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
interface IWwwApi {

    fun getAbsUrl(uri: String): String = PhotoNet.getWWWAbsUrl(uri)

    fun getPhotoPage(page: Int, callback: HttpCallback<PhotoPage>)

    fun getPhotoDetail(uri: String, callback: HttpCallback<PhotoDetail>)
}