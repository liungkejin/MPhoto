package cn.kejin.mphoto

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import cn.kejin.mphoto.net.entities.Photo
import cn.kejin.ximageview.XImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/16
 */
class PhotoDetailActivity : CustomStatusBarActivity()
{
    companion object {
        val TAG = "PhotoDetail"

        private var photoAdapter : PhotoAdapter? = null

        fun start(activity: Activity, adapter: PhotoAdapter) {
            photoAdapter = adapter

            val intent = Intent(activity, PhotoDetailActivity::class.java)
            activity.startActivity(intent)
        }
    }

    val viewPager : ViewPager by lazy { findViewById(R.id.viewPager) as ViewPager }
    val pageAdapter by lazy { PhotoPageAdapter() }

    override fun getLayoutId(): Int = R.layout.activity_photos_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById(R.id.back)?.setOnClickListener { finish() }
        viewPager.adapter = pageAdapter
    }

    inner class PhotoPageAdapter : PagerAdapter()
    {
        override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
            val view = layoutInflater.inflate(R.layout.layout_photo_detail, container, false)
            initializeView(view, photoAdapter?.get(position)?: Photo())

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
        }

        override fun getCount(): Int = (photoAdapter?.itemCount?:0)


        private fun initializeView(view: View, photo: Photo)
        {
            val xImageView = view.findViewById(R.id.xImageView) as XImageView
            val progress = view.findViewById(R.id.progress) as ProgressBar

            val avatarView = view.findViewById(R.id.avatar) as ImageView
            val userName = view.findViewById(R.id.name) as TextView

            val target = object: com.squareup.picasso.Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) { }

                override fun onBitmapFailed(errorDrawable: Drawable?) { }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    progress.visibility = View.GONE
                    xImageView.setImage(bitmap, false)
                }
            }

            val imageUrl = photo.getImage(1080)
            Log.e(TAG, "Url: $imageUrl")
            Picasso.with(this@PhotoDetailActivity).load(imageUrl).into(target)

            val avatarUrl = photo.author.avatar
            if (avatarUrl.isEmpty()) {
                avatarView.visibility = View.GONE
            }
            else {
                avatarView.visibility = View.VISIBLE
                Picasso.with(this@PhotoDetailActivity).load(photo.author.avatar).into(avatarView)
            }
            userName.text = photo.author.name

            view.findViewById(R.id.info)?.setOnClickListener {
                //
            }

            view.findViewById(R.id.download)?.setOnClickListener {
                Snackbar.make(xImageView, R.string.download_hint, Snackbar.LENGTH_INDEFINITE).setAction(R.id.download, {
                    //
                })
            }
        }
    }
}