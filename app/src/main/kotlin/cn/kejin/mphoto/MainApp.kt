package cn.kejin.mphoto

import android.Manifest
import android.app.Application
import android.graphics.Bitmap
import android.os.Environment
import android.os.Handler
import android.util.DisplayMetrics
import com.squareup.picasso.Cache
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/16
 */
class MainApp : Application() {

    companion object {
        /**
         * TODO
         */
        val DEBUG = true


        // App Directory
        val APP_DIR = "FreePhoto"
        val appDir by lazy {
            if (Environment.getExternalStorageDirectory()?.exists()?:false) {
                File(Environment.getExternalStorageDirectory(), APP_DIR)
            }
            else {
                instance.filesDir
            }
        }

        val appCacheDir : File
            get() {
                val file = File(appDir, "caches")
                if (file.exists()) return file
                return instance.cacheDir
            }

        /**
         * cache file
         */
        fun getCacheFile(url: String) : File {
            return File(appCacheDir, "temp${url.hashCode()}")
        }


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

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        displayMetrics.setTo(resources.displayMetrics)
    }

}