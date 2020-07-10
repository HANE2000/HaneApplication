package com.example.testtwitter4j.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testtwitter4j.R
import com.example.testtwitter4j.Utility.ErrorUtility
import com.example.testtwitter4j.Utility.ImageUtility
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.context.TweetStatusContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import twitter4j.StatusUpdate
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.UploadedMedia
import java.lang.NullPointerException
import kotlin.coroutines.CoroutineContext


//プロパティファイルを使う場合
class MainActivity : AppCompatActivity(), CoroutineScope {

    val twitter: Twitter = TwitterFactory().getInstance()

    //Coroutinesを扱うための設定（詳細は後述）
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // 画像1-4のBitmapを初期化
            TweetStatusContext().initializeBitmaps()

            // 画像選択
            upload_img_1.setOnClickListener {
                onClickUploadImg1()
            }
            upload_img_2.setOnClickListener {
                onClickUploadImg2()
            }
            upload_img_3.setOnClickListener {
                onClickUploadImg3()
            }
            upload_img_4.setOnClickListener {
                onClickUploadImg4()
            }


            tweet_button.setOnClickListener { //ID:buttonのボタンをクリックした際の処理
                onClickTweetButton()
            }
        } catch (e:Exception) {
            ErrorUtility.reportException(this, e)
        }
    }

    private var selectImageFrom = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        //val imageView = findViewById<ImageView>(R.id.imageView)
                        findViewById<ImageView>(selectImageFrom).setImageBitmap(image)

                        when (selectImageFrom) {
                            R.id.upload_img_1 -> {
                                TweetStatusContext.image1Bitmap = image
                            }
                            R.id.upload_img_2 -> {
                                TweetStatusContext.image2Bitmap = image
                            }
                            R.id.upload_img_3 -> {
                                TweetStatusContext.image3Bitmap = image
                            }
                            R.id.upload_img_4 -> {
                                TweetStatusContext.image4Bitmap = image
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateView() {
        //upload_img_1.setImageURI()
    }

    private fun onClickUploadImg1() {
        selectImageFrom = R.id.upload_img_1
        selectPhoto()
    }

    private fun onClickUploadImg2() {
        selectImageFrom = R.id.upload_img_2
        selectPhoto()
        //selectImageFrom = 0
    }

    private fun onClickUploadImg3() {
        selectImageFrom = R.id.upload_img_3
        selectPhoto()
        //selectImageFrom = 0
    }

    private fun onClickUploadImg4() {
        selectImageFrom = R.id.upload_img_4
        selectPhoto()
        //selectImageFrom = 0
    }

    private fun onClickTweetButton() {
        try {
            launch {
                message_text.text = "Now Sending..." //ここはメインスレッドで動作するのでViewの変更ができる

                // Tweet完了までの処理は別スレッドで行なう
                async(context = Dispatchers.IO) {
                    // ContextにTweet本文を格納
                    TweetStatusContext.sentence = tweet_edit.text.toString()
                    // TweetのStatusに、ツイートの本文を設定（ここに更に画像とか紐づけていく）
                    var update = StatusUpdate(TweetStatusContext.sentence)

                    // 添付画像1-4のID格納場所（初期値0）
                    var mediaIds = arrayListOf<Long>(0, 0, 0, 0)

                    // 添付画像1
                    if(TweetStatusContext.image1Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image1Bitmap!!, this@MainActivity)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        mediaIds[0] = media.mediaId
                        //update.media(file) // こいつのせいで添付ファイルが1枚になってたぽい
                    }
                    // 添付画像2
                    if(TweetStatusContext.image2Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image2Bitmap!!, this@MainActivity)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        mediaIds[1] = media.mediaId
                        //update.media(file)
                    }
                    // 添付画像3
                    if(TweetStatusContext.image3Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image3Bitmap!!, this@MainActivity)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        mediaIds[2] = media.mediaId
                        //update.media(file)
                    }
                    // 添付画像4
                    if(TweetStatusContext.image4Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image4Bitmap!!, this@MainActivity)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        mediaIds[3] = media.mediaId
                        //update.media(file)
                    }

                    // 添付画像1-4をTweetのステータスにセットする
                    update.setMediaIds(mediaIds[0], mediaIds[1], mediaIds[2], mediaIds[3])


                    //ツイートの投稿
                    // 2020/07/07ここで落ちた
                    // {"errors":[{"code":193,"message":"One or more of the uploaded media is too large."}]}
                    twitter.updateStatus(update)
                }.await() //.await()で通信処理が終わるまで待機

                //message_text.text = "Tweet completed."
                AppContext.getInstance().startActivity(this@MainActivity, TweetCompletedActivity::class.java)
            }
        } catch (e: Exception) {

        }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent,
            READ_REQUEST_CODE
        )

    }

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }



    override fun onDestroy() {
        job.cancel() //すべてのコルーチンキャンセル用
        super.onDestroy()
    }

}