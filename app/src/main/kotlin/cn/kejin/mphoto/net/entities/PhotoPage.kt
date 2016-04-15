package cn.kejin.mphoto.net.entities

import org.jsoup.Jsoup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/15
 */
class PhotoPage {

    companion object {
        fun parse(body: String): PhotoPage {
            val page = PhotoPage()

            val document = Jsoup.parse(body)
            val photoElements = document.getElementsByAttribute("data-photo-id")

            photoElements.forEach {
                page.photos.add(Photo.parse(it))
            }

            return page
        }
    }

    val photos = mutableListOf<Photo>()
}