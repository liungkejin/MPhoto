package cn.kejin.mphoto

import android.app.Application
import android.os.Environment
import android.os.Handler
import android.util.DisplayMetrics
import java.io.File

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/16
 */
class MainApp : Application() {

    companion object {
        // App Directory
        val APP_DIR = "FreePhoto"
        val appDir = File(Environment.getExternalStorageDirectory(), APP_DIR)
        val appCacheDir = File(appDir, "caches")

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

        /**
         * resources
         */
        fun color(id: Int) = instance.resources.getColor(id)

        fun string(id: Int) = instance.resources.getString(id)

        /**
         * cache file
         */
        fun newCacheFile() : File = File.createTempFile("fBzkd", "pcaq", appCacheDir)
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        displayMetrics.setTo(resources.displayMetrics)

        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs()
        }
    }
}