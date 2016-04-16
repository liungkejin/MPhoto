package cn.kejin.mphoto

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import cn.kejin.mphoto.net.entities.Photo
import cn.kejin.ximageview.XImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/16
 */
class PhotoDetailActivity : AppCompatActivity()
{
    companion object {
        val TAG = "PhotoDetail"

        private var photo : Photo = Photo();

        fun start(activity: Activity, p: Photo) {
            photo = p

            val intent = Intent(activity, PhotoDetailActivity::class.java)
            activity.startActivity(intent)
        }
    }

    val xImageView : XImageView by lazy { findViewById(R.id.xImageView) as XImageView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        val progress = findViewById(R.id.progress) as ProgressBar
//        Picasso.Builder(this).downloader(OkHttpDownloader(this, 10*1024))
        val imageUrl = photo.getImage(1080)
        Log.e(TAG, "Url: $imageUrl")
        Picasso.with(this)
                .load(imageUrl)
                .into(object: com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.e(TAG, "pre load")
                //
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                Log.e(TAG, "failed")
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                Log.e(TAG, "loaded")
                runOnUiThread {
                    progress.visibility = View.GONE
                    xImageView.setImage(bitmap, false)
                }
            }

        } )
    }
}