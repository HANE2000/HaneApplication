package com.example.testtwitter4j.utility

import android.widget.Toast
import com.example.testtwitter4j.bean.OutlayBean
import com.example.testtwitter4j.main.MainActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/**  */
class FirebaseUtility {
    /** 出費データを追加 */
    fun insertOutlayBean (outlayBean: OutlayBean) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("outlay")
        myRef.setValue(outlayBean)
        Toast.makeText(MainActivity(), "Firebase.database.getReference(message)", Toast.LENGTH_SHORT).show()
    }
}