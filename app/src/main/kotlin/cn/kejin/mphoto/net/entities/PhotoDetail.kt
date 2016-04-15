package cn.kejin.mphoto.net.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
class PhotoDetail : Photo() {

    companion object {
        fun parse(body: String): PhotoDetail {
            val detail = PhotoDetail()

            //
            return detail
        }
    }

    var info = Info()

    var stats = Stats()

    class Info {
        var publicTime = ""

        var dimensions = ""

        var camera_make = ""

        var camera_mode = ""

        var aperture = ""

        var shutter_speed = ""

        var focal_length = ""

        var iso = ""
    }

    class Stats {
        var downloads = ""

        var views = ""

        var likes = ""

        var rank = ""
    }
}