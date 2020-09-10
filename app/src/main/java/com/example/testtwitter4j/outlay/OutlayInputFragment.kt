package com.example.testtwitter4j.outlay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.testtwitter4j.R
import com.example.testtwitter4j.bean.OutlayBean
import com.example.testtwitter4j.main.MainActivity
import com.example.testtwitter4j.utility.ErrorUtility
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_outlay_input.*
import kotlinx.android.synthetic.main.fragment_outlay_input.view.*
import java.lang.Exception
import java.math.BigDecimal

class OutlayInputFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outlay_input, container, false)

        view.insertButton.setOnClickListener {
            onClickInsertButton()
        }

        return view
    }

    /** 「追加」ボタン */
    private fun onClickInsertButton () {
        try {
            val outlayBean = OutlayBean()
            outlayBean.category = categoryEdit.text.toString()
            outlayBean.amount = amountEdit.text.toString().toInt()

            // Write a message to the database
            val database = Firebase.database
            val myRef = database.getReference("server/saving-data/test-twitter4j").child("outlays")
            val pushMyRef = myRef.push()
            pushMyRef.setValue(outlayBean)

            Toast.makeText(activity, "outlayBean:$outlayBean", Toast.LENGTH_LONG).show()

            val mainActivity = activity as MainActivity
            mainActivity.replaceFragment(OutlayInputFragment())
        } catch (e:Exception) {
            ErrorUtility.reportException(context!!, e)
        }
    }

    companion object {

    }
}