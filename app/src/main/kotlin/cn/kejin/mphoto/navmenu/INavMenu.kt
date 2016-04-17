package cn.kejin.mphoto.navmenu

import android.support.v4.widget.DrawerLayout
import cn.kejin.mphoto.PhotoAdapter
import cn.kejin.mphoto.PhotoFragment
import cn.kejin.mphoto.net.IWwwApi
import cn.kejin.mphoto.pageloader.PageDriver

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/17
 */
interface INavMenu {

    fun onBackPressed(): Boolean

    fun openDrawer()

    fun closeDrawer()

    fun getDrawer() : DrawerLayout
}