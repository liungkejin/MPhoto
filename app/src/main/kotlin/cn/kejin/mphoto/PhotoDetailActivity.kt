package cn.kejin.mphoto

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import cn.kejin.mphoto.net.PhotoNet
import cn.kejin.mphoto.net.entities.Photo
import cn.kejin.net.okhttp.Net
import cn.kejin.ximageview.XImageView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.FileOutputStream
import java.io.IOException

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/16
 */
class PhotoDetailActivity : CustomStatusBarActivity()
{
    companion object {
        val TAG = "PhotoDetail"

        private var photoAdapter : PhotoAdapter? = null
        private var startPosition : Int = 0

        fun start(activity: Activity, adapter: PhotoAdapter, pos: Int) {
            photoAdapter = adapter
            startPosition = pos

            val intent = Intent(activity, PhotoDetailActivity::class.java)
            activity.startActivity(intent)
        }
    }

    val viewPager : ViewPager by lazy { findViewById(R.id.viewPager) as ViewPager }
    val pageAdapter by lazy { PhotoPageAdapter() }

    lateinit var avatarView : ImageView
    lateinit var userName : TextView
    lateinit var infoBtn : ImageView
    lateinit var downloadBtn: View


    override fun getLayoutId(): Int = R.layout.activity_photos_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById(R.id.back)?.setOnClickListener { finish() }

        avatarView = findViewById(R.id.avatar) as ImageView
        userName = findViewById(R.id.name) as TextView
        infoBtn = findViewById(R.id.infoBtn) as ImageView
        downloadBtn = findViewById(R.id.download)!!


        viewPager.adapter = pageAdapter

        viewPager.currentItem = startPosition
        viewPager.setOnScrollChangeListener{
            view, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e(TAG, "sx: $scrollX, sy: $scrollY, osx: $oldScrollX, osy: $oldScrollY")
        }

        viewPager.setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                setupImageInfoView(photoAdapter?.get(position)?: Photo())
            }
        })
    }

    fun setupImageInfoView(photo: Photo) {

        userName.text = photo.author.name

        val avatarUrl = photo.author.avatar
        if (avatarUrl.isEmpty()) {
            avatarView.visibility = View.GONE
        }
        else {
            avatarView.visibility = View.VISIBLE
            Picasso.with(this).load(photo.author.avatar).into(avatarView)
        }

        infoBtn.setOnClickListener {
            snack(it, "Info")
        }

        downloadBtn.setOnClickListener {
            snack(it, R.string.download_hint, Snackbar.LENGTH_INDEFINITE, R.id.download, View.OnClickListener {
                //
            })
        }
    }

    class ImageTarget(
            val progress: ProgressBar, val xImageView: XImageView) : com.squareup.picasso.Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) { }

        override fun onBitmapFailed(errorDrawable: Drawable?) { }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            progress.visibility = View.GONE
            xImageView.setActionListener(object : XImageView.SimpleActionListener() {
                override fun onSetImageFinished(view: XImageView?, success: Boolean, image: Rect?) {
                    bitmap?.recycle()
                }
            })
            xImageView.setImage(bitmap, true)
        }
    }

    inner class PhotoPageAdapter : PagerAdapter()
    {
        val mapCall: MutableMap<Int, Call> = mutableMapOf()

        override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
            val view = layoutInflater.inflate(R.layout.layout_photo_detail, container, false)
            initializeView(view, photoAdapter?.get(position)?: Photo(), position)

            container?.addView(view)
            return view
        }

        override fun isViewFromObject(view: View?, obj: Any?): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup?, position: Int, obj: Any?) {
            if (obj != null) {
                container?.removeView(obj as View)
            }
            mapCall.remove(position)?.cancel()
        }

        override fun getCount(): Int = (photoAdapter?.itemCount?:0)


        private fun initializeView(view: View, photo: Photo, position: Int)
        {
            val xImageView = view.findViewById(R.id.xImageView) as XImageView
            val progress = view.findViewById(R.id.progress) as ProgressBar

            val imageUrl = photo.getImage(720)
            error(TAG, "Url: $imageUrl")

            val cacheFile = MainApp.getCacheFile(imageUrl)
            if (cacheFile.exists()) {
                progress.visibility = View.GONE
                xImageView.setImage(cacheFile)
            }
            else {
                //TODO: XImageView Bug: loadingThread is null
                progress.visibility = View.VISIBLE
                val request = Request.Builder().url(imageUrl).get().build()
                val call = Net.requester.httpClient.newCall(request);
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        error(TAG, "Failed")
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        error(TAG, "Success")

                        val fos = FileOutputStream(cacheFile)

                        var bytes = ByteArray(1024)
                        val inputStream = response?.body()?.byteStream()
                        var read = inputStream?.read(bytes)?:0
                        while (read > 0) {
                            fos.write(bytes, 0, read)
                            fos.flush()

                            read = inputStream?.read(bytes)?:0
                        }
                        fos.close()
                        inputStream?.close()

                        post { progress.visibility=View.GONE; xImageView.setImage(cacheFile) }
                    }
                })

                mapCall.put(position, call)
            }

        }

    }
}