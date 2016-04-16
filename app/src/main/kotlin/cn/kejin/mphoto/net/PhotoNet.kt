package cn.kejin.mphoto.net

import cn.kejin.net.okhttp.Net
import org.jsoup.Jsoup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */

class PhotoNet : Net {
    private constructor()

    companion object {
        val TAG = "PhotoNet"

        val instance by lazy { PhotoNet() }

        val wwwApi : IWwwApi by lazy { WwwApiImpl() }

        val BASE_WWW_URL = "https://unsplash.com/"

        /**
         * get absolute URL
         */
        fun getWWWAbsUrl(uri: String): String =
                BASE_WWW_URL.removeSuffix("/") + "/" + uri.removePrefix("/")
    }
}