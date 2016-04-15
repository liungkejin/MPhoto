package cn.kejin.mphoto.net.entities

import org.jsoup.nodes.Element

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
open class Photo {
    companion object {
        fun parse(element: Element): Photo {
            val image = Photo()

            /**
             * 解析喜欢数
             */
            element.getElementsByAttribute("data-likes-count")?.forEach {
                image.like_num = it.attr("data-likes-count")
            }

            val authorElement = element.getElementsByClass("photo-description__author")

            authorElement.forEach {

            }

            return image
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
     */
    var image = "";

    var width = "0";

    var height = "0";


    /**
     * details of this image
     */
    var detail = "";

    /**
     * download url
     */
    var download = ""
}