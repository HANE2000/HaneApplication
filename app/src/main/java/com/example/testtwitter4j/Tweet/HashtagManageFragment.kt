package com.example.testtwitter4j.tweet

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testtwitter4j.R
import com.example.testtwitter4j.bean.TemplateBean
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.utility.ErrorUtility
import kotlinx.android.synthetic.main.fragment_hashtag_manage.*
import kotlinx.android.synthetic.main.fragment_hashtag_manage.view.*


// TODO: Rename parameter arguments, choose names that match


class HashtagManageFragment : Fragment() {
    private val ctx = AppContext.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hashtag_manage, container, false)

        try {
            // 「ADD」ボタン　の押下時リスナー
            view.template_add_button.setOnClickListener {
             addNewTemplate()
            }
            // 「COPY」ボタン の押下時リスナー
            view.template_copy_button.setOnClickListener {
                copySelectedTemplate()
            }
            // 「EDIT」ボタン の押下時リスナー
            view.template_edit_button.setOnClickListener {
                editSelectedTemplate()
            }
            // 「DELETE」ボタン の押下時リスナー
            view.template_delete_button.setOnClickListener {
                deleteSelectedTemplate()
            }

            // 「firebaseの最新リスト取得」ボタン押下時リスナー
            view.get_template_list_button.setOnClickListener {
                showTemplateList()
            }

        } catch (e: Exception) {
            ErrorUtility.reportException(context!!, e)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /**  */
    private fun addNewTemplate() {
        Toast.makeText(context,
            "TODO: 新規レコードを追加する処理を実装予定（まだしてない）",
            Toast.LENGTH_SHORT).show()
        /* TODO: 新規レコードを追加する処理実装
        val templateBean = TemplateBean(
            AppContext.userId,
            -1,
            "エボシカメレオン_Instagram",
            "#エボシカメレオン\n" +
                    "#カメレオン\n" +
                    "#爬虫類\n" +
                    "#爬虫類好き\n" +
                    "#cameleon\n" +
                    "#reptile #reptiles #reptilesofinstagram\n" +
                    "#pet #pets #petstagram"

        )
        // これbean情報をinsertするヤツ
        FirebaseUtility().insertTemplateBean(templateBean)

         */
    }

    /** 選択したレコードのvalueを表示、クリップボードコピーする */
    private fun copySelectedTemplate() {
        // リスト取得されてなかったら何も処理しない（エラー回避）
        if(ctx.getTemplateBeanList().size == 0) {
            Toast.makeText(context,
                "リスト取得して",
                Toast.LENGTH_SHORT).show()
            return
        }

        // なにも選択されてなかったら何も処理しない（エラー回避）
        if(ctx.getRecentSelectedTemplatePosition() < 0) {
            Toast.makeText(context,
                "テンプレ選択して",
                Toast.LENGTH_SHORT).show()
            return
        }

        // 選択したレコードのvalueを、EditTextに表示する
        template_edit.setText(ctx.getTemplateBeanList()[ctx.getRecentSelectedTemplatePosition()].value)
        // Copy text to clipboard
        val textToCopy = template_edit.text
        val clip = ClipData.newPlainText("RANDOM UUID",textToCopy)

        // MainActivity().clipboard.primaryClip = clip
        // クリップボードコピー、↑これだとエラー出た（primaryClipがValのためreassignedできない）
        // setPrimaryClip()なるモノあったので使ったらうまくいった
        AppContext.clipboard.setPrimaryClip(clip)
        Toast.makeText(context,
            "クリップボードにコピーした",
            Toast.LENGTH_SHORT).show()
    }

    /**  */
    private fun editSelectedTemplate() {

    }

    /**  */
    private fun deleteSelectedTemplate() {

    }


    /* 最新データを取得、リストを再表示 */
    private fun showTemplateList(): View? {
        // ※ ここadd()したらリストがどんどん重なって描画されたので、replace()使う
        // ※ MainActivityからreplaceFragment()呼んだら、Activityが既にdestroyedされてて落ちた
        childFragmentManager.beginTransaction().replace(R.id.list_container, HashtagListFragment()).commit();
        return view
    }


    companion object {
    }
}