package cn.kejin.mphoto

import cn.kejin.mphoto.net.PhotoNet
import cn.kejin.mphoto.net.entities.PhotoPage
import cn.kejin.net.okhttp.Net
import org.junit.Test

import org.junit.Assert.*

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class PhotoParseUnitTest {

    @Test
    fun parsePhotoPage() {
        val page = PhotoPage.parse(Net.requester.get(PhotoNet.BASE_WWW_URL)?.body()?.string()?:"")

        println(page)
    }
}