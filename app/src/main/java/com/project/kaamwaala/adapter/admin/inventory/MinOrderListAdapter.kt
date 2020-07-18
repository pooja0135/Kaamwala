package com.project.kaamwaala.adapter.admin.MinOrder


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.admin_stall_detail.MinOrderValueItem
import com.project.kaamwaala.OnItemDelete
import kotlinx.android.synthetic.main.inflate_min_order_value.view.*


class MinOrderListAdapter(
    private var modelList: List<MinOrderValueItem>,
    private var OnItemDelete: OnItemDelete
) :
    RecyclerView.Adapter<MinOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinOrderViewHolder {
        return MinOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_min_order_value
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MinOrderViewHolder, position: Int) {

        holder.itemView.tvData.setText(modelList.get(position).svalue)
        holder.itemView.ivClose.setOnClickListener { OnItemDelete.onClickDelete(modelList.get(position).Code,position) }
    }

    override fun getItemCount(): Int =modelList.size
}

class MinOrderViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

