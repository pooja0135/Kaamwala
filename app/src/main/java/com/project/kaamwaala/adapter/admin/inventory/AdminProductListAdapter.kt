package com.project.kaamwaala.adapter.admin.Product


import android.content.Intent
import android.graphics.Paint
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
import kotlinx.android.synthetic.main.inflate_admin_product_list.view.tvDiscount
import kotlinx.android.synthetic.main.inflate_admin_product_list.view.tvPrice
import kotlinx.android.synthetic.main.inflate_admin_product_list.view.tvSalePrice
import kotlinx.android.synthetic.main.inflate_inventory_list.view.*
import kotlinx.android.synthetic.main.inflate_price_list.view.*


class AdminProductListAdapter(
    private var modelList: List<ProductItem>
) :
    RecyclerView.Adapter<AdminProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        return AdminProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_admin_product_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        holder.itemView.cardviewProduct.setOnClickListener {
            holder.itemView.context.startActivity(Intent( holder.itemView.context, AdminProductDetailActivity::class.java).putExtra("productid",modelList.get(position).Code)) }

        holder.itemView.tvProductName.setText(modelList.get(position).Name)
        holder.itemView.tvSalePrice.setText("\u20B9"+modelList.get(position).SalePrice)
        holder.itemView.tvPrice.setText("\u20B9"+modelList.get(position).Price)
        if (modelList.get(position).Variants.equals("0"))
        {
            holder.itemView.tvVariant.visibility=View.GONE
        }
        else
        {
            holder.itemView.tvVariant.setText(modelList.get(position).Variants+" variants")
        }


        if (modelList.get(position).SalePrice.isEmpty()&&modelList.get(position).Price.isEmpty())
        {
            holder.itemView.tvPrice.visibility=View.GONE
            holder.itemView.tvSalePrice.visibility=View.GONE
            holder.itemView.tvDiscount.visibility=View.GONE
        }

        else  if (modelList.get(position).SalePrice.isEmpty())
        {
            holder.itemView.tvSalePrice.visibility=View.GONE
            holder.itemView.tvDiscount.visibility=View.GONE
        }
        else
        {
            holder.itemView.tvSalePrice.visibility=View.VISIBLE
            holder.itemView.tvDiscount.visibility=View.VISIBLE
            holder.itemView.tvPrice.setPaintFlags( holder.itemView.tvPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            var discount:Double=0.0
            discount=(modelList.get(position).Price.toDouble()-modelList.get(position).SalePrice.toDouble())*100/modelList.get(position).Price.toDouble()
            holder.itemView.tvDiscount.setText("-"+discount.toInt() + "%")

        }



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

class AdminProductViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

