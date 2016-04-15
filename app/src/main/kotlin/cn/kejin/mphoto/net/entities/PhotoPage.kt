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
            val photoElements = document.getElementsByClass("photo-grid")
            photoElements.forEach {
                println("Child Size: " + it.children().size)
                it.children().forEach {
                    page.photos.add(Photo.parse(it))
                }
            }

            return page
        }
    }

    val photos = mutableListOf<Photo>()

    override fun toString(): String {
        var string = ""
        photos.forEach {
            string += it.toString() + "\n\n"
        }

        return string
    }
}