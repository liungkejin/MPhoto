package cn.kejin.mphoto.pageloader

import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import cn.kejin.exrecyclerview.ExRecyclerView
import cn.kejin.mphoto.MainApp
import cn.kejin.mphoto.R

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/24
 */

/**
 * Control 'SwipeRefreshLayout' and 'ExRecyclerView' driver page
 */
class PageDriver(val refreshLayout: SwipeRefreshLayout?,
                 val exListView: ExRecyclerView,
                 val callback: ICallback) : PageController() {
    init {
        refreshLayout?.setOnRefreshListener { refresh() }
        refreshLayout?.setDistanceToTriggerSync(150)

        exListView.setOnLoadMoreListener {
            loadMore()
        };
    }

    override fun onLoading(page: Int) {
        callback.onLoading(page)
    }

    override fun onRefresh() {
        if (refreshLayout!= null && !refreshLayout.isRefreshing) {
            refreshLayout.post({ refreshLayout.isRefreshing = true })
        }
    }

    override fun onRefreshFinish(result: Result, lastResult: Result) {
        when (result) {
            Result.SUCCESS -> {
                if (!exListView.hasFooter(footer))
                    exListView.addFooter(footer)
                showLoading()
                exListView.endLoadMore()
            }

            Result.NO_MORE -> {
                if (!exListView.hasFooter(footer))
                    exListView.addFooter(footer)
                showNoMore()
            }

            Result.FAILED -> {
                callback.onRefreshFailed()
            }
        }
    }

    override fun onLoadMore(page: Int) {
        showLoading()
    }

    override fun onLoadMoreFinish(result: Result, lastResult: Result, loadingPage: Int) {
        when (result) {
            Result.SUCCESS -> {
                showLoading()
                exListView.endLoadMore()
            }
            Result.NO_MORE -> showNoMore()
            Result.FAILED -> showReload()
        }
    }

    override fun onFinish(result: Result, lastResult: Result, loadedPage: Int, loadingPage: Int) {
        refreshLayout?.post({ refreshLayout.isRefreshing = false })
    }

    /**
     * Footer control
     */
    private val footer = View.inflate(exListView.context, R.layout.layout_list_footer, null)

    private val loading by lazy { footer.findViewById(R.id.loading) }
    private val reload by lazy { footer.findViewById(R.id.reload) }
    private val noMore by lazy { footer.findViewById(R.id.noMore) }

    fun hideFooter() {
        footer.visibility = View.GONE
    }
    private fun showLoading() {
        footer.visibility = View.VISIBLE
        loading.visibility = View.VISIBLE
        reload.visibility = View.GONE
        noMore.visibility = View.GONE
    }

    private fun showReload() {
        footer.visibility = View.VISIBLE
        loading.visibility = View.GONE
        reload.visibility = View.VISIBLE
        noMore.visibility = View.GONE

        reload.findViewById(R.id.btn).setOnClickListener({ loadMore() })
    }

    private fun showNoMore() {
        footer.visibility = View.VISIBLE
        loading.visibility = View.GONE
        reload.visibility = View.GONE
        noMore.visibility = View.VISIBLE
    }

    interface ICallback {
        fun onLoading(page: Int)
        fun onRefreshFailed()
    }
}