package cn.kejin.mphoto.navmenu

import android.app.Fragment
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import cn.kejin.mphoto.AboutActivity
import cn.kejin.mphoto.PhotoFragment
import cn.kejin.mphoto.R
import cn.kejin.mphoto.net.IWwwApi

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/17
 */
class NavMenuCtrl(val activity: AppCompatActivity) : INavMenu, NavigationView.OnNavigationItemSelectedListener
{
    val drawerLayout by lazy { findViewById(R.id.drawLayout) as DrawerLayout }

    var curSelectedItem = R.id.latest
    val holderMap : Map<Int, PhotoFragment> = mapOf(
            Pair(R.id.latest, PhotoFragment(R.layout.fragment_photo_list, IWwwApi.SortType.LATEST)),

            Pair(R.id.popular, PhotoFragment(R.layout.fragment_photo_list,IWwwApi.SortType.POPULAR)),

            Pair(R.id.oldest, PhotoFragment(R.layout.fragment_photo_list,IWwwApi.SortType.OLDEST))
    )


    init {
        val navigationView = findViewById(R.id.navigationView) as NavigationView
        val toolbar = findViewById(R.id.toolbar) as Toolbar

        navigationView.setCheckedItem(R.id.latest)
        navigationView.setNavigationItemSelectedListener(this)

        activity.setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        selectItem(curSelectedItem)
    }

    fun findViewById(id: Int): View?
            = activity.findViewById(id)

    fun selectItem(id: Int)
            = replaceFragment(holderMap[id]?:PhotoFragment(R.layout.fragment_photo_list, IWwwApi.SortType.LATEST))

    private fun replaceFragment(fragment: Fragment)
            = activity.fragmentManager.beginTransaction()
                .replace(R.id.fragmentContent, fragment).addToBackStack(fragment.toString()).commit()

    override fun getDrawer(): DrawerLayout = drawerLayout

    override fun onBackPressed(): Boolean {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId?:0
        when (id) {
            R.id.latest,
            R.id.popular,
            R.id.oldest -> {
                if (id != curSelectedItem) {
                    selectItem(id)
                    curSelectedItem = id
                }
            }

            R.id.about -> {
                val intent = Intent(activity, AboutActivity::class.java)
                activity.startActivity(intent)
            }
        }

        closeDrawer()
        return true
    }

}