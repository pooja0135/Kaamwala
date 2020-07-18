package com.project.kaamwaala.adapter.admin.Category


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.city.CityItem
import com.project.kaamwaala.OnItemSelect
import kotlinx.android.synthetic.main.inflate_inventory_list.view.*


class CityAdapter(
    private var modelList: List<CityItem>,
   var callback: OnItemSelect
) :
    RecyclerView.Adapter<CityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_category
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {

          holder.itemView.tvCategory.setText(modelList.get(position).Name)

        holder.itemView.tvCategory.setOnClickListener { callback.onClick(modelList.get(position).Id,modelList.get(position).Name) }
    }

    override fun getItemCount(): Int =modelList.size
}

class CityViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

