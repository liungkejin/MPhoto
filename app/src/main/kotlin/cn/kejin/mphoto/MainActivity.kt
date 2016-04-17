package cn.kejin.mphoto

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.KeyEvent
import cn.kejin.mphoto.navmenu.INavMenu
import cn.kejin.mphoto.navmenu.NavMenuCtrl

class MainActivity : CustomStatusBarActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    companion object{
        val TAG = "MainActivity"
    }

    val navMenuCtrl : INavMenu by lazy { NavMenuCtrl(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navMenuCtrl.closeDrawer()
    }

    private var backFlag = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (!backFlag && !navMenuCtrl.onBackPressed()) {
                    backFlag = true
                    Snackbar.make(navMenuCtrl.getDrawer(), R.string.press_again_to_exit, Snackbar.LENGTH_SHORT).show()
                    postDelay({backFlag = false}, 2000)

                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}
