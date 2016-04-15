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
    }

    override fun baseWwwUrl(): String {
        return "https://unsplash.com/"
    }
}