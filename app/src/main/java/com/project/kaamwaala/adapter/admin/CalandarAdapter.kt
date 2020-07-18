package com.project.kaamwaala.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.CalendarModel
import kotlinx.android.synthetic.main.calendar_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class CalandarAdapter(
    private var modelList: MutableList<CalendarModel>,
    private var callback: OnItemAction
) :
    RecyclerView.Adapter<CalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalViewHolder {
        return CalViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.calendar_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalViewHolder, position: Int) {
        val model = modelList[position]
        holder.itemView.itemDate.text = model.date
        holder.itemView.itemDay.text = model.day

        if (model.isSelected) {
            holder.itemView.itemDay.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorAccent
                )
            )
            holder.itemView.itemDate.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorAccent
                )
            )
        } else {
            holder.itemView.itemDay.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.itemView.itemDate.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.itemView.setOnClickListener {
            callback.onItemSelect(model,modelList.get(position).selected_date)
            for (i in 0 until modelList.size) {
                modelList[i].isSelected = false
            }
            modelList[position].isSelected = true
            notifyDataSetChanged()
        }

    }


    override fun getItemCount(): Int = modelList.size

    interface OnItemAction {
        fun onItemSelect(model: CalendarModel,date:String)
    }

}


class CalViewHolder(viewW: View) :
    RecyclerView.ViewHolder(viewW)