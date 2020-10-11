package com.example.testtwitter4j.tweet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testtwitter4j.R
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.utility.ErrorUtility
import com.example.testtwitter4j.utility.FirebaseUtility
import kotlinx.android.synthetic.main.fragment_hashtag_manage.view.*
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match


class HashtagManageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hashtag_manage, container, false)

        // firebaseの最新リスト取得ボタン押下時リスナー
        view.get_template_list_button.setOnClickListener {
            showTemplateList()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    /* 最新データを取得、リストを再表示 */
    private fun showTemplateList(): View? {

        try {

            // 最新データを取得
            AppContext.getInstance()
                .setTemplateBeanList(ArrayList(FirebaseUtility().getTemplateRecord()))

            // コンテキストのTemplateBeanリスト
            val templateBeanList = AppContext.getInstance().getTemplateBeanList()


            // {カラム名, 内容} を入れる用
            val listData = ArrayList<HashMap<String, String>>()
            val hashTmp = HashMap<String, String>()

            // コンテキストのTemplateデータが0件の場合、トーストを出す
            if (templateBeanList.size < 1) {
                Toast.makeText(context,
                    "データの取得が完了していないか、\n" +
                            "データが登録されていないと思う。",
                    Toast.LENGTH_SHORT).show()
            }

            // dbの最新データそれぞれをHashMapにいれ、リスト化
            var i = 0
            for (templateBean in templateBeanList) {
                hashTmp.clear()
                hashTmp["index"] = i.toString()
                hashTmp["name"] = templateBean.name
                hashTmp["value"] = templateBean.value
                // ※ ここ、ただ add(hathTmp) すると、ぜんぶ末尾の要素に上書きされてしまう
                listData.add(HashMap(hashTmp))
                i ++
            }

            // adapterを定義
            // {index, テンプレ名, 内容} の配列を、ListView？のTextViewそれぞれにセット
            val adapter = SimpleAdapter(
                this.context, listData, R.layout.hashtag_list_item, arrayOf(
                    "index", "name", "value"
                ), intArrayOf(
                    R.id.temlate_index_view,
                    R.id.template_name_text,
                    R.id.temlate_value_hidden
                )
            )
            view!!.scroll_list.adapter = adapter

            // リスト内のアイテムの押下時リスナー
            view!!.scroll_list.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    when (view.id) {
                        // リストのいずれかの「COPY」ボタン押下時
                        R.id.template_copy_button -> Toast.makeText(
                            context,
                            listData[position].toString() + "のcopyボタンが押されました",
                            Toast.LENGTH_SHORT
                        ).show()

                        // リストのいずれかの「EDIT」ボタン押下時
                        R.id.template_edit_button -> Toast.makeText(
                            context,
                            listData[position].toString() + "のeditボタンが押されました",
                            Toast.LENGTH_SHORT
                        ).show()

                        // リストのいずれかの「DELETE」ボタン押下時
                        R.id.template_delete_button -> Toast.makeText(
                            context,
                            listData[position].toString() + "のdeleteボタンが押されました",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        } catch (e: Exception) {
            ErrorUtility.reportException(context!!, e)
        }
        return view
    }


    companion object {
    }
}