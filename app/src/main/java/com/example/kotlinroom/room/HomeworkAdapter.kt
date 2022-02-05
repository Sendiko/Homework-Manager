package com.example.kotlinroom.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinroom.R
import kotlinx.android.synthetic.main.list_idols.view.*
import java.util.ArrayList

class HomeworkAdapter(
    private val homeworks: ArrayList<Homework>, private var listener : OnAdapterListener
    ) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        return HomeworkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_idols, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val homework = homeworks[position]
        holder.view.text_title.text = homework.title
        holder.view.text_desc.text = homework.mapel
        holder.view.text_title.setOnClickListener {
            listener.onViewListener(homework)
        }
        holder.view.button_edit.setOnClickListener {
            listener.onUpdateListener(homework)
        }
        holder.view.button_delete.setOnClickListener {
            listener.onDeleteListener(homework)
        }
    }

    override fun getItemCount() = homeworks.size

    class HomeworkViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(list: List<Homework>){
        homeworks.clear()
        homeworks.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onViewListener(homework: Homework)
        fun onUpdateListener(homework: Homework)
        fun onDeleteListener(homework: Homework)
    }

}