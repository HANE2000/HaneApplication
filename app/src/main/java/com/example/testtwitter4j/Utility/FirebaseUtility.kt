package com.example.testtwitter4j.utility

import com.example.testtwitter4j.bean.OutlayBean
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers


/**  */
class FirebaseUtility {

    private val db = Firebase.database
    private val firestoreDb = FirebaseFirestore.getInstance()

    /** 出費データを追加 */
    fun insertOutlayBean (outlayBean: OutlayBean) {
        // Write a message to the database
        val myRef = db.getReference("server/saving-data/test-twitter4j/outlays")
            .child("${outlayBean.userId}")
        // outlays/[userId]直下にデータを追加
        val pushMyRef = myRef.push()
        pushMyRef.setValue(outlayBean)
    }


    fun getRecord (): List<OutlayBean> {
        lateinit var outlayBeanList: List<OutlayBean>
        Completable.fromAction {
        firestoreDb.collection("server/saving-data/test-twitter4j")
            .orderBy(OutlayBean::addedDate.name)
            // .orderBy(ScoreItem::registerTime.name, Query.Direction.DESCENDING)
            .limit(50)
            .get()

                /*
            .addOnCompleteListener {
                // progress.visibility = View.GONE
                if (it.isSuccessful) {
                    outlayBeanList = it.result!!.toObjects(OutlayBean::class.java)
                    // scoreAdapter.addAll(outlayBeanList)
                    // scoreAdapter.notifyDataSetChanged()
                } else {
                    outlayBeanList = it.result!!.toObjects(OutlayBean::class.java)
                    // context?.toast("失敗")
                }
            }

                 */
        }.subscribeOn(Schedulers.io())
            .blockingAwait()

        return outlayBeanList
    }
}