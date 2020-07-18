package com.project.kaamwaala.adapter.admin.inventory


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.admin.AdminCategoryDetailActivity
import com.project.kaamwaala.model.createstall.StoreCategoryItem
import com.project.kaamwaala.model.createstall.StoreCategoryResponse
import kotlinx.android.synthetic.main.inflate_adminstalllist.view.*
import kotlinx.android.synthetic.main.inflate_inventory_list.view.*


class InventoryListAdapter(
    private var modelList: List<StoreCategoryItem>
) :
    RecyclerView.Adapter<InventoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_inventory_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivCategory)

             holder.itemView.tvCategory.setText(modelList.get(position).CategoryName)

          holder.itemView.cardView.setOnClickListener { holder.itemView.context.startActivity(Intent( holder.itemView.context,
              AdminCategoryDetailActivity::class.java).putExtra("name",modelList.get(position).CategoryName).putExtra("image",modelList.get(position).ImagePath)
              .putExtra("store_id",modelList.get(position).StoreId).putExtra("category_id",modelList.get(position).Code)) }
    }

    override fun getItemCount(): Int =modelList.size
}

class InventoryViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

