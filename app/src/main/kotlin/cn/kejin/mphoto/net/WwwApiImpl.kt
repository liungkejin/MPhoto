package cn.kejin.mphoto.net

import cn.kejin.mphoto.net.entities.PhotoDetail
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.net.okhttp.HttpCallback
import cn.kejin.net.okhttp.HttpException
import cn.kejin.net.okhttp.Net
import okhttp3.Call
import okhttp3.Response

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
class WwwApiImpl : IWwwApi {
    override fun getUserPhotoPage(name: String,
                                  page: Int,
                                  sort: IWwwApi.SortType,
                                  callback: HttpCallback<PhotoPage>): Call? {
        val url = getSortUrl("$name?page=$page", sort)

        return Net.requester.get(url, object : HttpCallback<PhotoPage>(PhotoPage::class.java) {
            override fun onSuccess(call: Call?, resp: Response) {
                val photoPage = PhotoPage.parse(resp.body()?.string()?:"")
                post { onResponse(call, photoPage, null) }
            }

            override fun onResponse(call: Call?, model: PhotoPage?, exception: HttpException?) {
                callback.onResponse(call, model, exception)
            }
        })
    }

    override fun getPhotoPage(page: Int,
                              sort: IWwwApi.SortType,
                              callback: HttpCallback<PhotoPage>): Call? {
        var url = getSortUrl("?page=$page", sort)

        return Net.requester.get(url, object : HttpCallback<PhotoPage>(PhotoPage::class.java) {
            override fun onSuccess(call: Call?, resp: Response) {
                val photoPage = PhotoPage.parse(resp.body()?.string()?:"")
                post { onResponse(call, photoPage, null) }
            }

            override fun onResponse(call: Call?, model: PhotoPage?, exception: HttpException?) {
                callback.onResponse(call, model, exception)
            }
        })
    }

    override fun getPhotoDetail(uri: String, callback: HttpCallback<PhotoDetail>) {
        val url = getAbsUrl(uri)

        Net.requester.get(url, object : HttpCallback<PhotoDetail>(PhotoDetail::class.java) {
            override fun onSuccess(call: Call?, resp: Response) {
                val photoDetail = PhotoDetail.parse(resp.body()?.string()?:"")
                post { onResponse(call, photoDetail, null) }
            }

            override fun onResponse(call: Call?, model: PhotoDetail?, exception: HttpException?) {
                callback.onResponse(call, model, exception)
            }
        })
    }
}