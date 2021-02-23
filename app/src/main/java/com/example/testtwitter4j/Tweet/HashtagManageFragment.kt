package com.example.testtwitter4j.tweet

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testtwitter4j.R
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.databinding.FragmentHashtagManageBinding
import com.example.testtwitter4j.utility.ErrorUtility
import com.example.testtwitter4j.utility.FirebaseUtility
import kotlinx.android.synthetic.main.base_dialog.view.*
import kotlinx.android.synthetic.main.fragment_hashtag_manage.*
import kotlinx.android.synthetic.main.fragment_hashtag_manage.view.*
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match

class HashtagManageFragment : Fragment() {
    private lateinit var binding: FragmentHashtagManageBinding
    private val ctx = AppContext.getInstance()

    private lateinit var viewModel: HashtagManageViewModel

    companion object {
        fun newInstance() = HashtagManageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_hashtag_manage, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hashtag_manage, container, false)

        try {
            // 「ADD」ボタン　の押下時リスナー
            binding.templateAddButton.setOnClickListener {
                addNewTemplate()
                binding.dataEditDialog.new_data_name_edit.isFocusable = true;
                binding.dataEditDialog.new_data_name_edit.isFocusableInTouchMode = true;
                binding.dataEditDialog.new_data_name_edit.requestFocus();
            }

            // TODO: 「検索」エリアのEdit変更時リスナー

            // 「COPY」ボタン の押下時リスナー
            binding.templateCopyButton.setOnClickListener {
                copySelectedTemplate()
            }
            // 「EDIT」ボタン の押下時リスナー
            binding.templateEditButton.setOnClickListener {
                editSelectedTemplate()
            }
            // 「DELETE」ボタン の押下時リスナー
            binding.templateDeleteButton.setOnClickListener {
                deleteSelectedTemplate()
            }

            // 「firebaseの最新リスト取得」ボタン押下時リスナー
            binding.getTemplateListButton.setOnClickListener {
                showTemplateList()
            }

            // 「×（閉じる）」ボタン押下時リスナー
            binding.dataEditDialog.close_dialog_button.setOnClickListener {
                closeDialog()
            }

            //
            binding.dataEditDialog.new_data_add_button.setOnClickListener {
                addNewData()
            }

        } catch (e: Exception) {
            ErrorUtility.reportException(requireContext(), e)
        }
        //return view
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HashtagManageViewModel::class.java)
    }

    /** TODO: テンプレの新規データを作成・挿入 */
    private fun addNewTemplate() {

        requireView().data_edit_dialog.visibility = View.VISIBLE

        Toast.makeText(context,
            "TODO: 新規レコードを追加する処理を実装予定（まだしてない）",
            Toast.LENGTH_SHORT).show()
        /*
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
        if(ctx.getRecentSelectedTemplateRecord().uniqueKey.isEmpty()) {
            Toast.makeText(context,
                "テンプレ選択して",
                Toast.LENGTH_SHORT).show()
            return
        }

        // 選択したレコードのvalueを、EditTextに表示する
        template_edit.text = ctx.getRecentSelectedTemplateRecord().value
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

    /** TODO: 選択中のテンプレを編集する */
    private fun editSelectedTemplate() {
        Toast.makeText(context,
            "TODO: 選択中のテンプレを編集する処理を実装予定（まだしてない）",
            Toast.LENGTH_SHORT).show()
    }

    /** TODO: 選択中のテンプレを削除する */
    private fun deleteSelectedTemplate() {
        Toast.makeText(context,
            "TODO: 選択中のテンプレを削除する処理を実装予定（まだしてない）",
            Toast.LENGTH_SHORT).show()
    }


    /** 最新データを取得、リストを再表示 */
    private fun showTemplateList(): View? {
        // ※ ここadd()したらリストがどんどん重なって描画されたので、replace()使う
        // ※ MainActivityからreplaceFragment()呼んだら、Activityが既にdestroyedされてて落ちた
        childFragmentManager.beginTransaction().replace(R.id.list_container, HashtagListFragment()).commit()
        return view
    }

    /** dialogを閉じる */
    private fun closeDialog() {
        requireView().data_edit_dialog.new_data_name_edit.setText("")
        requireView().data_edit_dialog.new_data_value_edit.setText("")
        requireView().data_edit_dialog.visibility = View.INVISIBLE
    }

    /** データを挿入し、ダイアログを閉じる */
    private fun addNewData() {
        val name = requireView().data_edit_dialog.new_data_name_edit.text
        val value = requireView().data_edit_dialog.new_data_value_edit.text

        if(name.isNotEmpty() && value.isNotEmpty()){

            var isAlreadyExistsSameNameRecord = false
            // AppContextへまだRealtimeDBから取得してない場合、取得する
            // TODO: RealtimeDBから受信するのに時間差あって、この一発で取得できないヤツ
            if (ctx.getTemplateBeanList().size == 0) {
                ctx.setTemplateBeanList(ArrayList(FirebaseUtility().getTemplateRecord()))
            }
            // 既に同名レコードある場合、フラグtrueにする
            for (bean in ctx.getTemplateBeanList()) {
                if(bean.name == name.toString()){
                    isAlreadyExistsSameNameRecord = true
                }
            }
            // TODO: 既に同名のレコードある場合、UPDATEするかどうか確認ダイアログ表示しようか
            // TODO: UPDATE処理っぽいことも実装しよう
            if(isAlreadyExistsSameNameRecord) {
                Toast.makeText(
                    context,
                    "TODO: 既に同名のレコードある場合、上書きするかどうか確認ダイアログ表示しようか",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // レコードinsert
                FirebaseUtility().addOrUpdateNewTemplateBean(name.toString(), value.toString())
                // ダイアログ非表示に
                requireView().data_edit_dialog.visibility = View.INVISIBLE
                // 完了メッセージ（トースト）
                Toast.makeText(
                    context,
                    "name: ${name}, \n" +
                            "value: ${value} \n" +
                            "をinsertした",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "TitleとDetailセットでないとだめ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}