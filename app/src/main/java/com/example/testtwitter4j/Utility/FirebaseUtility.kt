package com.example.testtwitter4j.utility

import android.widget.Toast
import com.example.testtwitter4j.bean.OutlayBean
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.main.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


/** Firebase使う周辺のメソッドあれこれ */
class FirebaseUtility {

    private val db = Firebase.database
    // private val firestoreDb = FirebaseFirestore.getInstance()

    private val ctx = AppContext.getInstance()

    /** 出費データを追加 */
    fun insertOutlayBean(outlayBean: OutlayBean) {
        // Write a message to the database
        val myRef = db.getReference("server/test-twitter4j/outlays")
            .child("${outlayBean.userId}")
        // outlays/[userId]直下にデータを追加
        val pushMyRef = myRef.push()
        pushMyRef.setValue(outlayBean)
    }


    fun getRecord (): List<OutlayBean> {
        var outlayBeanList = ArrayList<OutlayBean>()

        // Attach a listener to read the data at our posts reference
        val myRef = db.getReference("server/test-twitter4j/outlays/HANEKW_")

        // データを取得するお決まりのやつ（リスナーを用意して二つのメソッドをオーバーライド）
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // dataSnapshotの子供（1ユーザーぶんのレコードList）でループする
                for (h in dataSnapshot.children) {
                    val value = h.value as Map<String, Any>
                    // MapをBeanに変換
                    val outlayBean = convertMapToOutlayBean(value)
                    // 1レコードずつListに詰める
                    outlayBeanList.add(outlayBean)
                }
                // contextを上書き
                ctx.setOutlayBeanList(outlayBeanList)
            }

            override fun onCancelled(error: DatabaseError) {
                // ...
                Toast.makeText(MainActivity(), "しっぱい", Toast.LENGTH_LONG).show()
            }
        })
        return ctx.getOutlayBeanList()
    }


    /** Map（Realtime Databaseから取得したうちの１レコード）を、OutlayBeanに変換する */
    fun convertMapToOutlayBean (map: Map<String, Any>): OutlayBean {
        val ret = OutlayBean()

        ret.userId = map["userId"] as String
        // ret.addedDate = map["addDate"] as Date
        ret.addedDate = Date()
        ret.category = map["category"] as String
        ret.amount = map["amount"] as Long

        return ret
    }
}