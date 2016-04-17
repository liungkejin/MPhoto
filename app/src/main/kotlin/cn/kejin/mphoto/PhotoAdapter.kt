package cn.kejin.mphoto

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.kejin.exrecyclerview.ExRecyclerAdapter
import cn.kejin.mphoto.net.IWwwApi
import cn.kejin.mphoto.net.PhotoNet
import cn.kejin.mphoto.net.entities.Photo
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.mphoto.pageloader.PageController
import cn.kejin.net.okhttp.HttpCallback
import cn.kejin.net.okhttp.HttpException
import com.squareup.picasso.Picasso
import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/17
 */

class PhotoAdapter(activity: Activity) :
        ExRecyclerAdapter<Photo, PhotoAdapter.ViewHolder>(activity) {

    companion object {
        var itemWidth = 200 // dp
    }

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
            Log.e(MainActivity.TAG, "AvatarUrl: $avatarUrl")


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

            Log.e(MainActivity.TAG, "ImageUrl: $imageUrl")

            findView(R.id.photoLayout)?.setOnClickListener {
                PhotoDetailActivity.start(activity, this@PhotoAdapter)
            }

            findView(R.id.userLayout)?.setOnClickListener {
                UserPhotosActivity.start(activity, model)
            }
        }
    }
}