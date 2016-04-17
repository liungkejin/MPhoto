package cn.kejin.net.okhttp

import android.util.Log
import okhttp3.*
import java.util.concurrent.TimeUnit


/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/28
 */
open class HttpRequester
{
    companion object {
        val TAG = "HttpRequester"

        val JSON_TYPE = MediaType.parse("application/json; charset:utf-8")

        val DEF_CONNECT_TIMEOUT_SEC = 20L
    }

    val httpClient : OkHttpClient;
    constructor(timeout: Long= DEF_CONNECT_TIMEOUT_SEC) {
        httpClient = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS).build()
    }

    constructor(okHttpClient: OkHttpClient) {
        httpClient = okHttpClient
    }

    /**
     * get sync
     */
    fun get(url: String) : Response? {

//        info("Method: GET sync, URL: $url")

        val builder = Request.Builder().url(url).get()

        return httpClient.newCall(builder.build()).execute()
    }

    /**
     * get method async
     */
    fun get(url: String, callback: Callback): Call? {

        Log.e(TAG, "Method: GET async, URL: $url")

        val builder = Request.Builder().url(url).get()
        var call = httpClient.newCall(builder.build());
        call.enqueue(callback)

        return call;
    }


    /**
     * post method sync
     */
    fun post(url: String, json: String): Response? {

//        info(TAG, "Method: POST, URL: $url, Json: $json")

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        builder.addHeader("Content-Type", "application/json").url(url).post(body);

        return httpClient.newCall(builder.build()).execute()
    }

    /**
     * post method async
     */
    fun post(url: String, json: String, callback: Callback): Call? {

//        info(TAG, "Method: POST, URL: $url, Json: $json")

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        builder.addHeader("Content-Type", "application/json").url(url).post(body);

        var call = httpClient.newCall(builder.build())
        call.enqueue(callback);

        return call;
    }
}