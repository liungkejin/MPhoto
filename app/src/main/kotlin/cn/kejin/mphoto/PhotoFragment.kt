package cn.kejin.mphoto

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Button
import cn.kejin.exrecyclerview.ExRecyclerView
import cn.kejin.mphoto.net.IWwwApi
import cn.kejin.mphoto.net.PhotoNet
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.mphoto.pageloader.PageController
import cn.kejin.mphoto.pageloader.PageDriver
import cn.kejin.net.okhttp.HttpCallback
import cn.kejin.net.okhttp.HttpException
import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/17
 */
class PhotoFragment(id: Int, val sortType: IWwwApi.SortType) : BaseFragment(id), PageDriver.ICallback
{
    lateinit var recyclerView: ExRecyclerView

    lateinit var layoutManager: StaggeredGridLayoutManager

    lateinit var photoAdapter: PhotoAdapter

    lateinit var refreshLayout : SwipeRefreshLayout

    lateinit var pageDriver: PageDriver

    lateinit var reloadBtn: Button

    var netCall : Call? = null

    val spanCount: Int
        get() {
            var count = MainApp.screenWidth / 200; //dp
            if (count < 1) count = 1

            val padding = 10;
            PhotoAdapter.itemWidth = (MainApp.screenWidth - (count + 1) * padding) / count

            return count
        }


    override fun initializeView(view: View) {
        refreshLayout = view.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        reloadBtn = view.findViewById(R.id.refresh) as Button
        recyclerView = view.findViewById(R.id.recyclerView) as ExRecyclerView

        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        photoAdapter = PhotoAdapter(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = photoAdapter

        pageDriver = PageDriver(refreshLayout, recyclerView, this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (photoAdapter.itemCount == 0) {
            refreshLayout.visibility = View.VISIBLE
            refreshLayout.isRefreshing = false
            refreshLayout.post{refreshLayout.isRefreshing=true}
            pageDriver.refresh()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        layoutManager.spanCount = spanCount
    }

    override fun onDestroy() {
        super.onDestroy()
        netCall?.cancel()
    }

    override fun onLoading(page: Int) {
        reloadBtn.visibility = View.GONE
        netCall = PhotoNet.wwwApi.getPhotoPage(page + 1, sortType,
                object : HttpCallback<PhotoPage>(PhotoPage::class.java) {
                    override fun onResponse(call: Call?, model: PhotoPage?, exception: HttpException?) {

                        var result = PageController.Result.SUCCESS
                        if (exception == null) {
                            if (model!!.photos.size < 20) {
                                result = PageController.Result.NO_MORE
                            }

                            if (pageDriver.isRefreshing()) {
                                photoAdapter.set(model.photos)
                            }
                            else {
                                photoAdapter.addAll(model.photos)
                            }
                        }
                        else {
                            result = PageController.Result.FAILED
                        }

                        pageDriver.finish(result)
                    }
                })
    }

    override fun onRefreshFailed() {
        if (photoAdapter.itemCount == 0) {
            reloadBtn.visibility = View.VISIBLE

            reloadBtn.setOnClickListener { pageDriver.refresh() }
        }

        snack(recyclerView, R.string.refresh_failed)
    }
}