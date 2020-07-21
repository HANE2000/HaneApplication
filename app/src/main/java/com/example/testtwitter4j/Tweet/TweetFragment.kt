package com.example.testtwitter4j.tweet

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testtwitter4j.R
import com.example.testtwitter4j.utility.ImageUtility
import com.example.testtwitter4j.context.TweetStatusContext
import com.example.testtwitter4j.main.MainActivity
import kotlinx.android.synthetic.main.fragment_tweet.*
import kotlinx.android.synthetic.main.fragment_tweet.view.*
import kotlinx.coroutines.*
import twitter4j.StatusUpdate
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.UploadedMedia
import kotlin.coroutines.CoroutineContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TweetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TweetFragment : Fragment() , CoroutineScope {

    private val twitter: Twitter = TwitterFactory().instance

    //Coroutinesを扱うための設定（詳細は後述）
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var contentResolver: ContentResolver

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentResolver = activity!!.contentResolver
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // fragmentの場合、onCreateView内で下記のようにviewを設定し、view.[id].setOnClickListener{}の形でクリックハンドラを書く
        val view = inflater.inflate(R.layout.fragment_tweet, container, false)


        // 画像1-4のBitmapを初期化
        TweetStatusContext().initializeBitmaps()

        // 画像選択
        view.upload_img_1.setOnClickListener {
            onClickUploadImg1()
        }
        view.upload_img_2.setOnClickListener {
            onClickUploadImg2()
        }
        view.upload_img_3.setOnClickListener {
            onClickUploadImg3()
        }
        view.upload_img_4.setOnClickListener {
            onClickUploadImg4()
        }


        view.tweet_button.setOnClickListener { //ID:buttonのボタンをクリックした際の処理
            onClickTweetButton()
        }
        return view
    }


    private var selectImageFrom = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != AppCompatActivity.RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        //val imageView = findViewById<ImageView>(R.id.imageView)
                        activity!!.findViewById<ImageView>(selectImageFrom).setImageBitmap(image)

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
                    Toast.makeText(activity, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context!!)
    }

    override fun onDetach() {
        super.onDetach()
    }

    /** 画像選択1 */
    private fun onClickUploadImg1() {
        selectImageFrom = R.id.upload_img_1
        selectPhoto()
    }
    /** 画像選択2 */
    private fun onClickUploadImg2() {
        selectImageFrom = R.id.upload_img_2
        selectPhoto()
        //selectImageFrom = 0
    }
    /** 画像選択3 */
    private fun onClickUploadImg3() {
        selectImageFrom = R.id.upload_img_3
        selectPhoto()
        //selectImageFrom = 0
    }
    /** 画像選択4 */
    private fun onClickUploadImg4() {
        selectImageFrom = R.id.upload_img_4
        selectPhoto()
        //selectImageFrom = 0
    }

    /** 「Tweet」ボタン押下イベント */
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
                    var mediaIds = arrayListOf<Long>()

                    // 添付画像1
                    if(TweetStatusContext.image1Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image1Bitmap!!, activity!!.applicationContext)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        //mediaIds[0] = media.mediaId
                        mediaIds.add(media.mediaId)
                        //update.media(file) // こいつのせいで添付ファイルが1枚になってたぽい
                    }
                    // 添付画像2
                    if(TweetStatusContext.image2Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image2Bitmap!!, activity!!.applicationContext)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        //mediaIds[1] = media.mediaId
                        mediaIds.add(media.mediaId)
                        //update.media(file)
                    }
                    // 添付画像3
                    if(TweetStatusContext.image3Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image3Bitmap!!, activity!!.applicationContext)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        //mediaIds[2] = media.mediaId
                        mediaIds.add(media.mediaId)
                        //update.media(file)
                    }
                    // 添付画像4
                    if(TweetStatusContext.image4Bitmap != null) {
                        val file = ImageUtility().bitmapToFile(TweetStatusContext.image4Bitmap!!, activity!!.applicationContext)
                        val media: UploadedMedia = twitter.uploadMedia(file)
                        //mediaIds[3] = media.mediaId
                        mediaIds.add(media.mediaId)
                        //update.media(file)
                    }

                    // 添付画像1-4をTweetのステータスにセットする
                    //update.setMediaIds(mediaIds[0], mediaIds[1], mediaIds[2], mediaIds[3])
                    update.setMediaIds(*mediaIds.toLongArray())


                    //ツイートの投稿
                    // 2020/07/07ここで落ちた
                    // {"errors":[{"code":193,"message":"One or more of the uploaded media is too large."}]}
                    twitter.updateStatus(update)
                }.await() //.await()で通信処理が終わるまで待機

                // message_text.text = "Tweet completed."

                // 2020/07/21 解決　↓↓
                // 以下のように、親Activityを取得して そこからメソッド呼ばないと、
                // IllegalStateException: Activity has been destroyed で落ちる（元のActivityの実体が不明なため）
                val mainActivity = activity as MainActivity
                mainActivity.replaceFragment(TweetCompletedFragment())
            }
        } catch (e: Exception) {

        }
    }

    /** 画像ファイルを選択するために外部アプリを開く */
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
}