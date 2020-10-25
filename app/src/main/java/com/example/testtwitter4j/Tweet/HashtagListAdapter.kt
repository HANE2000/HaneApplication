package com.example.testtwitter4j.tweet

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.testtwitter4j.R
import com.example.testtwitter4j.bean.TemplateBean
import com.example.testtwitter4j.context.AppContext
import com.example.testtwitter4j.main.MainActivity
import kotlinx.android.synthetic.main.fragment_hashtag_manage.view.*
import kotlinx.android.synthetic.main.hashtag_list_item.view.*

class HashtagListAdapter(
    private val templateBeanList: ArrayList<TemplateBean>,
    private val view: View
) :
    RecyclerView.Adapter<HashtagListAdapter.HashtagListViewHolder>() {

    class HashtagListViewHolder(
        private val hashTagListItem: LinearLayout
    ) : RecyclerView.ViewHolder(hashTagListItem)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HashtagListAdapter.HashtagListViewHolder {
        // create a new view
        val listItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.hashtag_list_item, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters
        return HashtagListViewHolder(listItem)
    }

    //Viewのコンテンツを置き換える（LayoutManagerによって呼び出される）
    override fun onBindViewHolder(holder: HashtagListViewHolder, position: Int) {
        holder.itemView.template_index_view.text = position.toString()
        holder.itemView.template_name_text.text = templateBeanList[position].name
        holder.itemView.template_value_hidden.text = templateBeanList[position].value
        holder.itemView.template_item.setOnClickListener {
            // AppContextに、選択したレコードのpositionを格納しとく
            AppContext.getInstance().setRecentSelectedTemplatePosition(position)

            holder.itemView.template_item.setBackgroundColor(R.drawable.run_button_background)
            for (i in 0..templateBeanList.size) {
                if (i == position) {
                    // TODO: 選択中のレコードだけスタイル変えろ

                }
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = templateBeanList.size
}
