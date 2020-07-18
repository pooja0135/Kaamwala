package com.project.kaamwaala.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.customer.ProductPriceListAdapter
import com.project.kaamwaala.OnItemAdd
import com.project.kaamwaala.OnItemMinus
import com.project.kaamwaala.activity.customer.product.ProductDetailActivity
import com.project.kaamwaala.model.customer.customer_product.CustomerProductItem
import kotlinx.android.synthetic.main.inflate_category_list.view.*
import kotlinx.android.synthetic.main.inflate_product_list.view.*


class ProductListAdapter(
    private var modelList: List<CustomerProductItem>,
    private var OnItemAdd: OnItemAdd,
    private var OnItemMinus: OnItemMinus
) :
    RecyclerView.Adapter<ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_product_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {



        holder.itemView.rvPrice.apply {
            holder.itemView.rvPrice.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductPriceListAdapter(modelList.get(position).ProductPriceItem,OnItemAdd,OnItemMinus) }

        holder.itemView.tvProductName.setText(modelList.get(position).Name)
        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivProduct)


        holder.itemView.ivProduct.setOnClickListener { holder.itemView.context.startActivity(Intent(holder.itemView.context,ProductDetailActivity::class.java).putExtra("productid",modelList.get(position).Id)) }


    }

    override fun getItemCount(): Int =modelList.size
}

class ProductViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

