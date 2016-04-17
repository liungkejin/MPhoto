package cn.kejin.mphoto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cn.kejin.mphoto.net.entities.Photo

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/17
 */
class UserPhotosActivity : CustomStatusBarActivity()
{
    companion object {
        private var photo : Photo = Photo();

        fun start(activity: Activity, p: Photo) {
            photo = p

            val intent = Intent(activity, UserPhotosActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int  = R.layout.activity_user_photos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}