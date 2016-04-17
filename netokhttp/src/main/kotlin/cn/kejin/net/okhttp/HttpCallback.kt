package cn.kejin.net.okhttp

import android.os.Handler
import android.view.View

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */
abstract class HttpCallback<Model> (val cls : Class<Model>,
                                    val gson: Gson = Net.gson,
                                    val handler: Handler = Net.handler) : Callback
{
    protected fun post(r : ()->Unit ) {
        handler.post({ r() });
    }

    override fun onFailure(call: Call?, e: IOException?) {
        post {
            onResponse(call, null, HttpException(HttpException.E_IO, e?.message?:"UNKNOWN"))
        }
    }

    override fun onResponse(call: Call?, resp: Response?) {

        if (resp == null || resp.code() != 200) {
            val code = resp?.code()?:HttpException.E_UNKNOWN
            val msg = resp?.message()?:"UNKNOWN"
            post {
                onResponse(call, null, HttpException(code, msg))
            }
        }
        else {
            onSuccess(call, resp)
        }
    }

    /**
     * this method run in thread
     */
    open fun onSuccess(call: Call?, resp: Response) {

        var exception : HttpException? = null;

        try {
            // IOException
            val body = resp.body()?.string() ?: ""
            // JsonSyntaxException
            val model: Model = gson.fromJson(body, cls) ?: throw Exception("parse json failed")

            onResponse(call, model, null)
        }
        catch(e: IOException) {
            exception = HttpException(HttpException.E_IO, e.message?:"process json failed")
        }
        catch(e: JsonSyntaxException) {
            exception = HttpException(HttpException.E_GSON_SYNTAX, e.message?:"process json failed")
        }
        catch(e: HttpException) {
            exception = e
        }
        catch(e: Exception) {
            exception = HttpException(HttpException.E_UNKNOWN, e.message?:"UNKNOWN")
        }

        if (exception != null) {
            post {
                onResponse(call, null, exception)
            }
        }
    }


    /**
     * failed if exception != null
     * run on main thread
     */
    abstract fun onResponse(call: Call?, model : Model?, exception: HttpException?=null)
}