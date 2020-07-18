package com.project.kaamwaala.adapter.admin.Product


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.admin.AdminProductDetailActivity
import com.project.kaamwaala.model.stall_category_product.ProductItem
import kotlinx.android.synthetic.main.inflate_admin_product_list.view.*
import kotlinx.android.synthetic.main.inflate_inventory_list.view.*


class ProductListAdapter(
    private var modelList: List<ProductItem>
) :
    RecyclerView.Adapter<ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_admin_product_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.cardviewProduct.setOnClickListener {
            holder.itemView.context.startActivity(Intent( holder.itemView.context, AdminProductDetailActivity::class.java).putExtra("productid",modelList.get(position).Code)) }

        holder.itemView.tvProductName.setText(modelList.get(position).Name)
        holder.itemView.tvSalePrice.setText(modelList.get(position).SalePrice)
        holder.itemView.tvPrice.setText(modelList.get(position).Price)
        holder.itemView.tvVariant.setText(modelList.get(position).Variants)
        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivProduct)

    }

    override fun getItemCount(): Int =modelList.size
}

class ProductViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

