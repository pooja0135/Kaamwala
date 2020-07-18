package com.project.kaamwaala.adapter.admin.customer


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.OnItemCreateProduct
import com.project.kaamwaala.OnItemStock
import com.project.kaamwaala.R
import com.project.kaamwaala.model.stall_category_product.ProductPriceItem
import kotlinx.android.synthetic.main.inflate_product_detail_price_list.view.*
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvDescription
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvDiscount
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvPrice
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvSalePrice


class ProductDetailPriceListAdapter(
    private var modelList: List<ProductPriceItem>,
    private var OnItemStock: OnItemStock

) :
    RecyclerView.Adapter<ProductDetailPriceViewHolder>() {

    var cartvalue:Int=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailPriceViewHolder {
        return ProductDetailPriceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_product_detail_price_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductDetailPriceViewHolder, position: Int) {

        holder.itemView.tvPrice.setText("\u20B9"+modelList.get(position).Price)
        holder.itemView.tvSalePrice.setText("\u20B9"+modelList.get(position).SalePrice)

       if( modelList.get(position).Describe.isEmpty())
       {
          holder.itemView.tvDescription.visibility=View.GONE
       }
        else
       {
           holder.itemView.tvDescription.setText(modelList.get(position).Describe)

       }




        if (modelList.get(position).SalePrice.isEmpty())
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
            holder.itemView.tvDiscount.setText("-"+String.format("%.2f", discount) + "%")

        }

        holder.itemView.cardview.setOnClickListener { OnItemStock.onClickStock(modelList.get(position).Code,"","") }

    }

    override fun getItemCount(): Int =modelList.size
}

class ProductDetailPriceViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

