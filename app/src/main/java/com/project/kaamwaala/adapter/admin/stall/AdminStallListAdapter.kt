package com.project.kaamwaala.adapter.admin.stall


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.R
import com.project.kaamwaala.model.adminstalllist.StoreItem
import com.project.kaamwaala.OnItemClickStall
import kotlinx.android.synthetic.main.inflate_adminstalllist.view.*


class AdminStallListAdapter(
    private var modelList: List<StoreItem>,
    private var OnItemClickStall: OnItemClickStall
) :
    RecyclerView.Adapter<AdminStallViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminStallViewHolder {
        return AdminStallViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_adminstalllist
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdminStallViewHolder, position: Int) {


        holder.itemView.cardview.setOnClickListener {

            OnItemClickStall.onClickStall(modelList.get(position).StoreId)


        }

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).StoreImageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivStallImage)

        holder.itemView.tvStallName.setText(modelList.get(position).StoreName)
    }

    override fun getItemCount(): Int =modelList.size
}

class AdminStallViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

