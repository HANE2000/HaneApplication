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
import com.example.testtwitter4j.utility.FirebaseUtility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
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
            outlayBeanList = FirebaseUtility().getRecord()

            Toast.makeText(activity, "outlayBeanList:${outlayBeanList}", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            ErrorUtility.reportException(context!!, e)
        }
    }

    /** 「追加」ボタン */
    private fun onClickInsertButton () {
        try {
            val outlayBean = OutlayBean(
                "HANEKW_", // TODO: TwitterID(@無し) 取得処理（今はとりまベタ書き）
                Date(), // 追加日時(現在の時刻)
                categoryEdit.text.toString(), // 項目名
                amountEdit.text.toString().toLong() // 値段
            )

            // データ挿入処理
            FirebaseUtility().insertOutlayBean(outlayBean)
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