package com.example.testtwitter4j.tweet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.testtwitter4j.R
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.main.MainActivity
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

    /** 選択したレコードのvalueを表示、クリップボードコピーする */
    private fun copySelectedTemplate() {
        // 選択したレコードのvalueを、EditTextに表示する
        template_edit.setText(ctx.getTemplateBeanList()[ctx.getRecentSelectedTemplatePosition()].value)
        // Copy text to clipboard
        val textToCopy = template_edit.text
        val clip = ClipData.newPlainText("RANDOM UUID",textToCopy)

        // MainActivity().clipboard.primaryClip = clip
        // クリップボードコピー、↑これだとエラー出た（primaryClipがValのためreassignedできない）
        // setPrimaryClip()なるモノあったので使ったらうまくいった
        AppContext.clipboard.setPrimaryClip(clip)
    }

    /**  */
    private fun editSelectedTemplate() {

    }

    /**  */
    private fun deleteSelectedTemplate() {

    }


    /* 最新データを取得、リストを再表示 */
    private fun showTemplateList(): View? {
        childFragmentManager.beginTransaction().add(R.id.list_container, HashtagListFragment()).commit();
        return view
    }


    companion object {
    }
}