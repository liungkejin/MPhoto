package cn.kejin.mphoto.net.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
class Author {

    var avatar = ""

    var name = ""

    /**
     * home page url
     */
    var home = ""

    override fun toString(): String {
        return "Name: $name\nAvatar: $avatar\nHome: $home"
    }
}