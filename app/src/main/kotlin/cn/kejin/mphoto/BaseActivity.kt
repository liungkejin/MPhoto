package cn.kejin.mphoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.ProgressBar

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/7
 */

/**
 * 主要意图:
 *  1. startFlag 防止连续多次启动 activity
 *  2. lastUserState 保存上次用户状态, 在 onResume 时检查用户状态是否变化,
 *  3. progressDialog
 *  4. handler = MainApp.handler
 */
abstract class BaseActivity: AppCompatActivity(){

    companion object {
        val clz = 0
    }
    /**
     * prevent start activity twice
     */
    protected var startFlag = false;

    /**
     * last user state
     */
//    var lastUserState = MainApp.account

    /**
     * progress dialog
     */
    var progressDialog : AlertDialog? = null

    /**
     * application handler
     */
    val handler = MainApp.handler

    /*******************************************FUN***************************************/
    abstract fun getLayoutId() : Int;

    open fun getMenuLayoutId(): Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val layoutId = getMenuLayoutId()
        if (layoutId > 0) {
            menuInflater.inflate(layoutId, menu)
            return true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        startFlag = false;
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 防止点击两次
     */
    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (!startFlag) {
            super.startActivityForResult(intent, requestCode, options)
            startFlag = true;
            // 防止异常情况发生
            postDelay({startFlag=false}, 1000)
        }
    }

//    /**
//     * show progress dialog
//     */
//    fun showProgressDialog(cancelable: Boolean=true, outSideCancel: Boolean=false) {
//        if (progressDialog == null) {
//            progressDialog = AlertDialog.Builder(this, R.style.TransDialog)
//                    .setView(ProgressBar(this))
//                    .setCancelable(cancelable)
//                    .create()
//        }
//
//        progressDialog?.setCanceledOnTouchOutside(outSideCancel)
//        progressDialog?.setCancelable(cancelable)
//
//        if (progressDialog?.isShowing ?:true) {
//            return;
//        }
//
//        progressDialog?.show()
//    }
//
//    fun dismissProgressDialog() {
//        progressDialog?.dismiss()
//    }

    /**
     * post
     */
    fun post (r : () -> Unit) = MainApp.handler.post { r() }

    fun postDelay (r : () -> Unit, delay : Long) = MainApp.handler.postDelayed(r, delay)

    /**
     * inflate view
     */
    fun inflateView(id:Int) : View? = if (id > 0) layoutInflater.inflate(id, null) else null

    /**
     * start activity
     */
    fun startActivity(clz : Class<*>) = startActivity(Intent(this, clz))

    /**
     * 打开浏览器
     */
    fun startBrowser(uri : String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

}