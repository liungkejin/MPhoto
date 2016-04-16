package cn.kejin.mphoto.net.entities

import android.net.Uri
import android.util.Log

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
class Author {

    /**
     * https://images.unsplash.com/profile-1458723679261-a5ef32cb2a04?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=32&w=32&s=18e1410b919fc280a0f85a4738ebb3a6
     */
    private var ixlib = "rb-0.3.5"
    private var fm = "jpg"
    private var s = ""

    var avatar = ""
        set(value) {
            val uri = Uri.parse(value)

            ixlib = uri.getQueryParameter("ixlib")
            fm = uri.getQueryParameter("fm")
            s = uri.getQueryParameter("s")

            field = "${uri.scheme}://${uri.host}/${uri.path}"
        }

    fun getAvatar(width: Int, q: Int = 100): String {
        if (avatar.isEmpty()) {
            return ""
        }

//        Log.e("Author: ", "$avatar?ixlib=$ixlib&q=$q&fm=$fm&s=$s&w=$width")
        return "$avatar?ixlib=$ixlib&q=$q&fm=$fm&s=$s&w=$width"
    }

    var name = ""

    /**
     * home page url
     */
    var home = ""

    override fun toString(): String {
        return "Name: $name\nAvatar: $avatar\nHome: $home"
    }
}