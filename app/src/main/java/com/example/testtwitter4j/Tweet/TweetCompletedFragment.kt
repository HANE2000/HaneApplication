package com.example.testtwitter4j.tweet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtwitter4j.R
import com.example.testtwitter4j.utility.ErrorUtility
import com.example.testtwitter4j.context.TweetStatusContext
import com.example.testtwitter4j.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.base_completed_layout.view.*
import kotlinx.android.synthetic.main.fragment_tweet_completed.*
import kotlinx.android.synthetic.main.fragment_tweet_completed.view.*
import kotlinx.android.synthetic.main.fragment_tweet_completed.view.base_completed_layout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TweetCompletedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TweetCompletedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        try {
            //super.onCreate(savedInstanceState)

            // Inflate the layout for this fragment
            // fragmentの場合、onCreateView内で下記のようにviewを設定し、view.[id].setOnClickListener{}の形でクリックハンドラを書く
            val view = inflater.inflate(R.layout.fragment_tweet_completed, container, false)

            // fragment部分の各コンポーネントも、親Activityから取得しないと、base_completed_layoutが正常に得られない
            view.base_completed_layout.theme_text.text =
                "Tweet完了"
            view.base_completed_layout.detail_text.text =
                "以下の内容でTweetが完了しました。\n" +
                    "本文 ：${TweetStatusContext.sentence}\n" +
                    "画像1：${TweetStatusContext.image1Bitmap.toString()}\n" +
                    "画像2：${TweetStatusContext.image2Bitmap.toString()}\n" +
                    "画像3：${TweetStatusContext.image3Bitmap.toString()}\n" +
                    "画像4：${TweetStatusContext.image4Bitmap.toString()}\n"
            view.base_completed_layout.button.text = "TOPへ"

            view.base_completed_layout.button.setOnClickListener {
                //AppContext.getInstance().startActivity(this, MainActivity::class.java)
                (activity as MainActivity).replaceFragment(TweetFragment())
            }

            // 2020/07/23
            // setText()とかsetOn○○Lister()をしたViewをreturnしないと、それらが画面側に適用されないのがわかった
            return view
        } catch (e: Exception) {
            //ErrorUtility.reportException(this, e)
            ErrorUtility.reportException(activity as MainActivity, e)
        }

        return inflater.inflate(R.layout.fragment_tweet_completed, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TweetCompletedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TweetCompletedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context!!)
    }

    override fun onDetach() {
        super.onDetach()
    }
}