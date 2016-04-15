package cn.kejin.net.okhttp

import android.os.Handler
import com.google.gson.Gson
import com.google.gson.GsonBuilder

interface Net
{
    companion object {
        /**
         * default gson
         */
        val gson: Gson by lazy { GsonBuilder().create() }

        /**
         * default handler
         */
        val handler : Handler = Handler()

        /**
         * default http requester
         */
        val requester : HttpRequester by lazy { HttpRequester() }
    }

}
