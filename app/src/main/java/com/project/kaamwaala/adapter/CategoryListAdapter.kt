package com.project.kaamwaala.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.stall.StallListActivity
import com.project.kaamwaala.model.category.CategoryItem
import kotlinx.android.synthetic.main.inflate_category_list.view.*



class CategoryListAdapter(
    private var modelList: List<CategoryItem>
) :
    RecyclerView.Adapter<CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_category_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.itemView.cardview.setOnClickListener {
            holder.itemView.context.startActivity(Intent( holder.itemView.context,
            StallListActivity::class.java).putExtra("category_id",modelList.get(position).CategoryId).putExtra("category_name",modelList.get(position).CategoryName))
        }

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).CategoryImageURL)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivCategory)



        holder.itemView.tvCategory.setText(modelList.get(position).CategoryName)

    }

    override fun getItemCount(): Int =modelList.size
}

class CategoryViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

