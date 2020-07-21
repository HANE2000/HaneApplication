package com.example.testtwitter4j.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testtwitter4j.R
import com.example.testtwitter4j.tweet.TweetCompletedFragment
import com.example.testtwitter4j.tweet.TweetFragment
import com.example.testtwitter4j.utility.ErrorUtility
import kotlinx.coroutines.*
import twitter4j.Twitter
import twitter4j.TwitterFactory
import kotlin.coroutines.CoroutineContext


//プロパティファイルを使う場合
class MainActivity : AppCompatActivity(), CoroutineScope {

    var fragmentManager: FragmentManager = supportFragmentManager
    var fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
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
            R.id.menu_item02 -> {
                // 処理
                replaceFragment(TweetCompletedFragment())
                true
            }
            R.id.menu_item03 ->                 // 処理
                true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // LinearLayoutのコンテナをfragment指定 + replaceする
    // 引数は 「new Fragmentを継承しているクラスのインスタンス()」
    fun replaceFragment(fragment: Fragment) {
        // 2020/07/16 以下で落ちる
        // kotlin.UninitializedPropertyAccessException: lateinit property fragmentManager has not been initialized
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragment_container,
            fragment
        ) // ここFragmentのインスタンスはsupport.v4のものであること！！
        fragmentTransaction.commit()
    }

}