package com.example.testtwitter4j.Tweet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testtwitter4j.R
import com.example.testtwitter4j.Utility.ErrorUtility
import com.example.testtwitter4j.activity.MainActivity
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.context.TweetStatusContext
import kotlinx.android.synthetic.main.activity_tweet_completed.*
import kotlinx.android.synthetic.main.base_completed_layout.view.*

class TweetCompletedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tweet_completed)

            base_completed_layout.theme_text.text = "Tweet完了"
            base_completed_layout.detail_text.text = "以下の内容でTweetが完了しました。\n" +
                    "本文 ：${TweetStatusContext.sentence}\n" +
                    "画像1：${TweetStatusContext.image1Bitmap.toString()}\n" +
                    "画像2：${TweetStatusContext.image2Bitmap.toString()}\n" +
                    "画像3：${TweetStatusContext.image3Bitmap.toString()}\n" +
                    "画像4：${TweetStatusContext.image4Bitmap.toString()}\n"
            base_completed_layout.button.text = "TOPへ"

            base_completed_layout.button.setOnClickListener {
                AppContext.getInstance().startActivity(this, MainActivity::class.java)
            }
        } catch (e: Exception) {
            ErrorUtility.reportException(this, e)
        }
    }
}