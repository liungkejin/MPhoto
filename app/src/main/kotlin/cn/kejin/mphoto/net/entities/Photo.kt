package cn.kejin.mphoto.net.entities

import android.net.Uri
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
open class Photo {
    companion object {
        fun parse(element: Element): Photo {
            val photo = Photo()

            /**
             * 解析 author
             */
            element.getElementsByClass("photo-description__author").forEach {
                it.getElementsByTag("img").forEach {
                    photo.author.avatar = it.attr("src")
                }
                it.getElementsByTag("h2").forEach {
                    it.getElementsByTag("a").forEach {
                        photo.author.home = it.attr("href")
                        photo.author.name = it.text()
                    }
                }
            }

            /**
             * 解析 Photo
             */
            element.getElementsByClass("photo__image-container").forEach {
                photo.detail = it.attr("href")
                it.getElementsByTag("img").forEach {
                    photo.width = it.attr("data-width")
                    photo.height = it.attr("data-height")
                    photo.image = it.attr("src")
                }
            }

            /**
             * 解析喜欢数
             */
            element.getElementsByAttribute("data-likes-count").forEach {
                photo.like_num = it.attr("data-likes-count")
            }

            return photo
        }
    }

    /**
     * author
     */
    var author = Author();


    /**
     * like number
     */
    var like_num = "0"


    /**
     * image url
     * https://images.unsplash.com/photo-1458724338480-79bc7a8352e4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&w=1080&fit=max&s=0e8fe82e7f50091319fdc635582bf62d
     */
    private var ixlib = "rb-0.3.5"
    private var fm = "jpg"
    private var s = ""

    var image = ""
        set(value) {
//            val uri = Uri.parse(value)
//
//            ixlib = uri.getQueryParameter("ixlib")
//            fm = uri.getQueryParameter("fm")
//            s = uri.getQueryParameter("s")
//
//            field = uri.host
            field = value
        }

    var width = "0";

    var height = "0";


    /**
     * details of this image
     */
    var detail = "";

    override fun toString(): String {
        return "$author\nImage: $image?ixlib=$ixlib&fm=$fm&s=$s\nWidth: $width, Height: $height\nDetail:$detail"
    }
}