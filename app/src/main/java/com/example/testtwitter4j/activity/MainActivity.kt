package com.example.testtwitter4j.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testtwitter4j.R
import com.example.testtwitter4j.Tweet.TweetCompletedActivity
import com.example.testtwitter4j.Tweet.TweetFragment
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
import kotlin.coroutines.CoroutineContext


//プロパティファイルを使う場合
class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private val twitter: Twitter = TwitterFactory().instance

    //Coroutinesを扱うための設定（詳細は後述）
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()

        } catch (e:Exception) {
            ErrorUtility.reportException(this, e)
        }
    }

    /* メニューをActivity上に設置する */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_buttons, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* メニューが選択された時の処理 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item01 -> {
                // 処理
                Toast.makeText(this, "Function.replaceFragment()", Toast.LENGTH_SHORT).show()

                // 試作メソッド
                replaceFragment(TweetFragment())
                true
            }
            R.id.menu_item02 ->                 // 処理
                true
            R.id.menu_item03 ->                 // 処理
                true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // LinearLayoutのコンテナをfragment指定 + replaceする
    // 引数は 「new Fragmentを継承しているクラスのインスタンス()」
    fun replaceFragment(fragment: Fragment) {
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragment_container,
            fragment
        ) // ここFragmentのインスタンスはsupport.v4のものであること！！
        fragmentTransaction.commit()
    }

}