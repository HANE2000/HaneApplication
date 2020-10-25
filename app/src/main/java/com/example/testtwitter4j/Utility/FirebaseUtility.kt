package com.example.testtwitter4j.utility

import android.widget.Toast
import com.example.testtwitter4j.bean.OutlayBean
import com.example.testtwitter4j.bean.TemplateBean
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.main.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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

    /** テンプレートデータを追加 */
    fun insertTemplateBean(templateBean: TemplateBean) {
        // Write a message to the database
        val myRef = db.getReference("server/test-twitter4j/templates")
            .child("${templateBean.userId}")
        // outlays/[userId]直下にデータを追加
        val pushMyRef = myRef.push()
        pushMyRef.setValue(templateBean)
    }

    // ~/outlays/[ユーザーID] を取得（Firebase）
    fun getOutlayRecord (): List<OutlayBean> {
        var outlayBeanList = ArrayList<OutlayBean>()

        // Attach a listener to read the data at our posts reference
        val myRef = db.getReference("server/test-twitter4j/outlays/${AppContext.userId}")

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

    // ~/template/[ユーザーID] を取得（Firebase）
    fun getTemplateRecord (): List<TemplateBean> {
        var templateBeanList = ArrayList<TemplateBean>()

        // Attach a listener to read the data at our posts reference
        val myRef = db.getReference("server/test-twitter4j/templates/${AppContext.userId}")
        // server/test-twitter4j/templates/${AppContext.userId} がホントは正しいパスだけど、間違えてテストデータ登録しちゃった

        // データを取得するお決まりのやつ（リスナーを用意して二つのメソッドをオーバーライド）
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // dataSnapshotの子供（1ユーザーぶんのレコードList）でループする
                for (h in dataSnapshot.children) {
                    val value = h.value as Map<String, Any>
                    // MapをBeanに変換
                    val templateBean = convertMapToTemplateBean(value)
                    // 1レコードずつListに詰める
                    templateBeanList.add(templateBean)
                }
                // contextを上書き
                ctx.setTemplateBeanList(templateBeanList)
            }

            override fun onCancelled(error: DatabaseError) {
                // ...
                Toast.makeText(MainActivity(), "しっぱい", Toast.LENGTH_LONG).show()
            }
        })
        return ctx.getTemplateBeanList()
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

    /** Map（Realtime Databaseから取得したうちの１レコード）を、TemplateBeanに変換する */
    fun convertMapToTemplateBean (map: Map<String, Any>): TemplateBean {
        val ret = TemplateBean()

        ret.index = map["index"] as Long
        ret.name = map["name"] as String
        ret.userId = map["userId"] as String
        ret.value = map["value"] as String

        return ret
    }
}