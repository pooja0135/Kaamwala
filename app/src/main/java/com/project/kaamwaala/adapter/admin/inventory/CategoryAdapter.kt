package com.project.kaamwaala.adapter.admin.Category


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.category.CategoryItem
import com.project.kaamwaala.OnItemSelect
import kotlinx.android.synthetic.main.inflate_inventory_list.view.*


class CategoryAdapter(
    private var modelList: List<CategoryItem>,
   var callback: OnItemSelect
) :
    RecyclerView.Adapter<CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_category
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

          holder.itemView.tvCategory.setText(modelList.get(position).CategoryName)

             holder.itemView.tvCategory.setOnClickListener { callback.onClick(modelList.get(position).CategoryId,modelList.get(position).CategoryName) }
    }

    override fun getItemCount(): Int =modelList.size
}

class CategoryViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

