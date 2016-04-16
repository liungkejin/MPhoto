package cn.kejin.mphoto

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.kejin.exrecyclerview.ExRecyclerAdapter
import cn.kejin.exrecyclerview.ExRecyclerView
import cn.kejin.mphoto.net.PhotoNet
import cn.kejin.mphoto.net.entities.Photo
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.mphoto.pageloader.PageController
import cn.kejin.mphoto.pageloader.PageDriver
import cn.kejin.net.okhttp.HttpCallback
import cn.kejin.net.okhttp.HttpException
import com.squareup.picasso.Picasso
import okhttp3.Call

class MainActivity : AppCompatActivity() {

    companion object{
        val TAG = "MainActivity"
    }

    val recyclerView : ExRecyclerView by lazy {
        findViewById(R.id.recyclerView) as ExRecyclerView
    }

    val photoAdapter : PhotoAdapter by lazy {
        PhotoAdapter()
    }

    lateinit var layoutManager : StaggeredGridLayoutManager

    val pageDriver : PageDriver by lazy {
        PageDriver(findViewById(R.id.swipeRefresh) as SwipeRefreshLayout, recyclerView, pageCallback)
    }

    val screenWidth : Int
        get() {
            return MainApp.pxToDp(resources.displayMetrics.widthPixels.toFloat())
        }

    var spanCount = 2
        set(value) {
            var v = value
            if (v <= 0) {
                v = 1
            }

            val padding = 10;
            itemWidth = (screenWidth - (v+1)*padding)/v
            Log.e(TAG, "ItemWidth: $itemWidth")
            field = v
        }

    var itemWidth = 200 // dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeView()
    }

    private fun initializeView() {
        /**
         * 计算 span count, 以180dp 计算
         */

        Log.e(TAG, "screenWidth: $screenWidth")
        spanCount = screenWidth / 200

        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = photoAdapter

        pageDriver.refresh()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        Log.e(TAG, "Config Changes")

        spanCount = screenWidth / 200
        layoutManager.spanCount = spanCount
    }

    private val pageCallback = object : PageDriver.ICallback {
        override fun onLoading(page: Int) {
            PhotoNet.wwwApi.getPhotoPage(page+1,
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
            Snackbar.make(recyclerView, "Load Failed!", Snackbar.LENGTH_INDEFINITE).setAction("Retry", {
                pageDriver.refresh()
            }).show()
        }
    }

    inner class PhotoAdapter : ExRecyclerAdapter<Photo, PhotoAdapter.ViewHolder>(this) {
        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.bindView(data[position], position)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
            return ViewHolder(inflateView(R.layout.list_item_photo, parent))
        }

        inner class ViewHolder(view: View): ExViewHolder<Photo>(view) {
            override fun bindView(model: Photo, pos: Int) {
                val like = findView(R.id.like_num) as TextView
                like.text = model.like_num

                val name = findView(R.id.name) as TextView
                name.text = model.author.name

                val avatar = findView(R.id.avatar) as ImageView
                val avatarUrl = model.author.getAvatar(64)
                if (avatarUrl.isEmpty()) {
                    avatar.visibility = View.GONE
                }
                else {
                    avatar.visibility = View.VISIBLE
                    Picasso.with(activity)
                            .load(avatarUrl)
                            .error(R.mipmap.ic_default_avatar)
                            .placeholder(R.mipmap.ic_default_avatar)
                            .into(avatar)
                }
                Log.e(TAG, "AvatarUrl: $avatarUrl")


                val width: Int = model.width.toInt()
                val height: Int = model.height.toInt()
                val itemHeight: Float = itemWidth.toFloat()*1f / width * height


                val image = findView(R.id.photo) as ImageView
                val imageUrl = model.getImage((itemWidth*1.5f).toInt())
                image.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, MainApp.dpToPx(itemHeight))
                Picasso.with(activity)
                        .load(imageUrl)
                        .error(R.mipmap.test)
                        .placeholder(R.mipmap.test).into(image)

                Log.e(TAG, "ImageUrl: $imageUrl")

                image.setOnClickListener {
                    PhotoDetailActivity.start(activity, model)
                }
            }
        }
    }
}
