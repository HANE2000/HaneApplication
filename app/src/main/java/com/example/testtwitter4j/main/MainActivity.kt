package com.example.testtwitter4j.main

//import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testtwitter4j.R
import com.example.testtwitter4j.outlay.OutlayInputFragment
import com.example.testtwitter4j.tweet.TweetFragment
import com.example.testtwitter4j.utility.ErrorUtility
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.footer_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import twitter4j.Twitter
import twitter4j.TwitterFactory
import kotlin.coroutines.CoroutineContext

import android.content.ClipData
import android.content.ClipboardManager
import androidx.core.content.ContextCompat
import com.example.testtwitter4j.context.AppContext


//プロパティファイルを使う場合
class MainActivity : AppCompatActivity(), CoroutineScope {

    var fragmentManager: FragmentManager = supportFragmentManager
    var fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
    private val twitter: Twitter = TwitterFactory().instance

    /*
    val config = TwitterConfig.Builder(this)
        .logger(DefaultLogger(Log.DEBUG))
        .twitterAuthConfig(
            TwitterAuthConfig(
                // 暫定ベタ書き（ここ適宜取得する処理作る必要あり）
                "gAbSxVEZi0dcnFdfyPgCSm4k1", // CONSUMER_KEY,
                "bcSoFJTDxmvKEV6lZw2ObIE5mFtWYVf6pdoOwt4ElOnViA1DQ3" // CONSUMER_SECRET
            )
        )
        .debug(true)
        .build()

     */

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

            footer.button_1.setOnClickListener {
                replaceFragment(R.id.fragment_container, TweetFragment())
            }

            footer.button_2.setOnClickListener {
                replaceFragment(R.id.fragment_container, OutlayInputFragment())
            }

            footer.button_3.setOnClickListener {
                Toast.makeText(this,
                    "TODO: なにか別の機能を実装予定（なにつくろう）",
                    Toast.LENGTH_SHORT).show()
            }

            footer.button_4.setOnClickListener {
                Toast.makeText(this,
                    "TODO: なにか別の機能を実装予定（なにつくろう）",
                    Toast.LENGTH_SHORT).show()
            }

            // Firebaseの初期化
            FirebaseApp.initializeApp(this)
            //mAuth = FirebaseAuth.getInstance()

            // Get the clipboard system service
            AppContext.clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        } catch (e: Exception) {
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
                Toast.makeText(this, "未実装1", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_item02 -> {
                // 処理
                // Write a message to the database
                val database = Firebase.database
                val myRef = database.getReference("message")
                myRef.setValue("Hello, World!")
                Toast.makeText(this, "Firebase.database.getReference(message)", Toast.LENGTH_SHORT)
                    .show()
                true
            }
            R.id.menu_item03 -> {
                // 処理
                Toast.makeText(this, "未実装3", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // LinearLayoutのコンテナをfragment指定 + replaceする
    // 引数は 「new Fragmentを継承しているクラスのインスタンス()」
    fun replaceFragment(layout: Int, fragment: Fragment) {
        // 2020/07/16 以下で落ちる
        // kotlin.UninitializedPropertyAccessException: lateinit property fragmentManager has not been initialized
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            layout,
            fragment
        ) // ここFragmentのインスタンスはsupport.v4のものであること！！
        fragmentTransaction.commit()
    }


    fun getAppContext(): Context? {
        return this
    }

}