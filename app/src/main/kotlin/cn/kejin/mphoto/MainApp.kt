package cn.kejin.mphoto

import android.app.Application
import android.os.Handler
import android.util.DisplayMetrics

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/16
 */
class MainApp : Application() {

    companion object {
        // global context
        lateinit var instance : MainApp
            private set

        // global handler
        val handler : Handler = Handler()

        // for  dpToPx, pxToDp
        val displayMetrics = DisplayMetrics()

        val screenWidth : Int
            get() {
                return pxToDp(instance.resources.displayMetrics.widthPixels.toFloat())
            }

        /**
         * Dp to px
         */
        fun dpToPx(dp : Float) : Int
                = (dp * displayMetrics.density + 0.5f).toInt();

        /**
         * Px to Dp
         */
        fun pxToDp(px : Float) : Int
                = (px / displayMetrics.density + 0.5f).toInt();
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        displayMetrics.setTo(resources.displayMetrics)
    }
}