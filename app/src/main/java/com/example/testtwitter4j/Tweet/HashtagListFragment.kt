package com.example.testtwitter4j.tweet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtwitter4j.R
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.main.MainActivity
import com.example.testtwitter4j.utility.ErrorUtility
import com.example.testtwitter4j.utility.FirebaseUtility
import kotlinx.android.synthetic.main.fragment_hashtag_list.view.*
import kotlinx.android.synthetic.main.fragment_hashtag_manage.view.*
import java.util.ArrayList


class HashtagListFragment : Fragment() {

    private lateinit var scrollRecycler: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hashtag_list, container, false)

        try {

            // 最新データを取得
            AppContext.getInstance()
                .setTemplateBeanList(ArrayList(FirebaseUtility().getTemplateRecord()))

            // コンテキストのTemplateBeanリスト
            val templateBeanList = AppContext.getInstance().getTemplateBeanList()


            // コンテキストのTemplateデータが0件の場合、トーストを出す
            if (templateBeanList.size < 1) {
                Toast.makeText(context,
                    "データの取得が完了していないか、\n" +
                            "データが登録されていないと思う。",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,
                    "リストのデータを最新化した（${templateBeanList.size}件）",
                    Toast.LENGTH_SHORT).show()
            }

            viewManager = LinearLayoutManager(MainActivity())
            viewAdapter = HashtagListAdapter(templateBeanList, view)

            scrollRecycler = (view.scroll_recycler as RecyclerView).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
            }

        } catch (e: Exception) {
            ErrorUtility.reportException(context!!, e)
        }

        return view
    }

    companion object {

    }
}