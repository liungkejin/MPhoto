package cn.kejin.net.okhttp


/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/28
 */
open class HttpException (val code: Int, val msg: String) : Exception(msg)
{
    companion object {
        val TAG = "HttpException"

        /**
         * exception type code
         */
        val E_IO = -9997

        val E_GSON_SYNTAX = -9998

        val E_UNKNOWN = -9999
    }

    override fun toString(): String {
        return "HttpException: ($code, $msg)"
    }
}