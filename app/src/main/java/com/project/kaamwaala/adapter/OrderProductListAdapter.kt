package com.project.kaamwaala.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.R
import com.project.kaamwaala.model.customer.order.OrderItem
import kotlinx.android.synthetic.main.inflate_order_product_list.view.*
import kotlinx.android.synthetic.main.inflate_order_product_list.view.ivProduct
import kotlinx.android.synthetic.main.inflate_order_product_list.view.tvProductName


class OrderProductListAdapter(
    private var modelList: List<OrderItem>
) :
    RecyclerView.Adapter<OrderProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHolder {
        return OrderProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_order_product_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderProductViewHolder, position: Int) {

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivProduct)

        holder.itemView.tvProductName.setText(modelList.get(position).ProductName)
        holder.itemView.tvPrice.setText("\u20B9"+modelList.get(position).TotalPrice)
        holder.itemView.tvDescription.setText(modelList.get(position).Description)
        holder.itemView.tvQuantity.setText(modelList.get(position).Quantity)
    }

    override fun getItemCount(): Int =modelList.size
}

class OrderProductViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

