package com.example.testtwitter4j.context

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import java.io.InputStream

class TweetStatusContext {
    companion object {

        /** ツイート本文 */
        var sentence:String = ""

        /** アップロードする画像のURIのString */
        var image1Uri:Uri = Uri.parse("")
        var image2Uri:Uri = Uri.parse("")
        var image3Uri:Uri = Uri.parse("")
        var image4Uri:Uri = Uri.parse("")

        /** 添付画像のBitmap */
        var image1Bitmap:Bitmap? = null
        var image2Bitmap:Bitmap? = null
        var image3Bitmap:Bitmap? = null
        var image4Bitmap:Bitmap? = null

    }

    /** 添付画像Bitmap1-4を全て初期化する（Tweet完了後にまた残存しているケースを防ぐため） */
    fun initializeBitmaps () {
        image1Bitmap = null
        image2Bitmap = null
        image3Bitmap = null
        image4Bitmap = null
    }

}