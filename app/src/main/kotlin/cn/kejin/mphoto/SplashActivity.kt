package cn.kejin.mphoto

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/18
 */

/**
 * 请求权限
 */
class SplashActivity : CustomStatusBarActivity(), EasyPermissions.PermissionCallbacks
{
    companion object {
        val TAG = "Splash"

        const val RC_STORAGE_PERM = 12
//        const val RC_INTERNET_PERM = 13
    }

    override fun getLayoutId(): Int
            = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    fun initialize() {

        val storagePer = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if(EasyPermissions.hasPermissions(this, storagePer)) {
            createAppDirs()
        }
        else {
            EasyPermissions.requestPermissions(this, "Need write storage permission", RC_STORAGE_PERM, storagePer)
        }
    }

    fun createAppDirs() {
        if (!MainApp.appDir.exists()) {
            MainApp.appDir.mkdirs()
        }

        if (!MainApp.appCacheDir.exists()) {
            MainApp.appCacheDir.mkdirs()
        }

        initializePicasso()
        startMainActivity()
    }

    fun initializePicasso() {
//        val picasso = Picasso.Builder(this)
//                .defaultBitmapConfig(Bitmap.Config.RGB_565)
//                .loggingEnabled(MainApp.DEBUG)
//                .downloader(OkHttpDownloader(MainApp.appCacheDir))
//                .indicatorsEnabled(MainApp.DEBUG)
//                .memoryCache(LruCache(10 * 1024 * 1024))
//                .build()
//
//        Picasso.setSingletonInstance(picasso)
    }

    fun startMainActivity() {
        postDelay({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        var ps = ""
        perms?.forEach { ps += "$it\n" }

        Log.e(TAG, "Denied RC: $requestCode: $ps")

        initializePicasso()
        startMainActivity()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {

        var ps = ""
        perms?.forEach { ps += "$it\n" }

        Log.e(TAG, "Granted RC: $requestCode: $ps")

        when (requestCode) {
            RC_STORAGE_PERM -> {
                createAppDirs()
            }
        }
    }
}