package com.example.testtwitter4j.outlay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.testtwitter4j.R
import com.example.testtwitter4j.bean.OutlayBean
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.main.MainActivity
import com.example.testtwitter4j.utility.ErrorUtility
import com.example.testtwitter4j.utility.FirebaseUtility
import kotlinx.android.synthetic.main.fragment_outlay_input.*
import kotlinx.android.synthetic.main.fragment_outlay_input.view.*
import java.lang.Exception
import java.util.*
// import io.reactivex.Completable
// import io.reactivex.schedulers.Schedulers

class OutlayInputFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        val db = Firebase.database
        // Attach a listener to read the data at our posts reference
        val myRef = db.getReference("server/test-twitter4j/outlays/${AppContext.userId}")
        // データを取得するお決まりのやつ（リスナーを用意して二つのメソッドをオーバーライド）
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(activity, "受信したよ", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // ...
                Toast.makeText(MainActivity(), "しっぱい", Toast.LENGTH_LONG).show()
            }
        })

         */

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outlay_input, container, false)

        view.resetButton.setOnClickListener {
            onClickResetButton()
        }

        // 「追加」ボタン押下ハンドラ
        view.insertButton.setOnClickListener {
            onClickInsertButton()
        }

        return view
    }

    private fun onClickResetButton () {
        lateinit var outlayBeanList: List<OutlayBean>
        try {
            outlayBeanList = FirebaseUtility().getOutlayRecord()

            Toast.makeText(activity, "outlayBeanList:${outlayBeanList}", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            ErrorUtility.reportException(context!!, e)
        }
    }

    /** 「追加」ボタン */
    private fun onClickInsertButton () {
        try {
            val outlayBean = OutlayBean(
                AppContext.userId, // TODO: TwitterID(@無し) 取得処理（今はとりまベタ書き）
                Date(), // 追加日時(現在の時刻)
                categoryEdit.text.toString(), // 項目名
                amountEdit.text.toString().toLong() // 値段
            )

            // データ挿入処理
            FirebaseUtility().insertOutlayBean(outlayBean)
            Toast.makeText(activity, "outlayBean:$outlayBean", Toast.LENGTH_LONG).show()

            val mainActivity = activity as MainActivity
            mainActivity.replaceFragment(R.id.fragment_container, OutlayInputFragment())
        } catch (e:Exception) {
            ErrorUtility.reportException(context!!, e)
        }
    }


    companion object {

    }
}