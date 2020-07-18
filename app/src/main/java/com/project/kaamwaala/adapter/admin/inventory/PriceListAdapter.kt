package com.project.kaamwaala.adapter.admin.Price


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.OnItemDeletePrice
import com.project.kaamwaala.OnItemEditPrice
import com.project.kaamwaala.OnItemStock
import com.project.kaamwaala.R
import com.project.kaamwaala.model.stall_category_product.ProductPriceItem
import kotlinx.android.synthetic.main.inflate_price_list.view.*
import kotlinx.android.synthetic.main.inflate_price_list.view.ivEdit


class PriceListAdapter(
    private var modelList: List<ProductPriceItem>,
    private var OnItemDeletePrice: OnItemDeletePrice,
    private var OnItemEditPrice: OnItemEditPrice,
    private var OnItemStock: OnItemStock

) :
    RecyclerView.Adapter<PriceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        return PriceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_price_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {

        holder.itemView.tvPrice.setText("\u20B9"+modelList.get(position).Price)
        holder.itemView.tvSalePrice.setText("\u20B9"+modelList.get(position).SalePrice)
        holder.itemView.tvDescribe.setText(modelList.get(position).Describe)

        if (modelList.get(position).IsStock.equals("True"))
        {
            holder.itemView.switchButton.isChecked=true
        }
        else
        {
            holder.itemView.switchButton.isChecked=false
        }


        if (modelList.get(position).SalePrice.isEmpty())
        {
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


        holder.itemView.ivEdit.setOnClickListener {
            OnItemEditPrice.onClickEditPrice(modelList.get(position).Price,modelList.get(position).SalePrice,modelList.get(position).Describe,modelList.get(position).Code)
        }

        holder.itemView.ivDelete.setOnClickListener {
            OnItemDeletePrice.onClickDeletePrice(modelList.get(position).Code)
        }



        holder.itemView.switchButton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    OnItemStock.onClickStock(modelList.get(position).Code,"1",modelList.get(position).Price)
                }
                else
                {
                    OnItemStock.onClickStock(modelList.get(position).Code,"0",modelList.get(position).Price)
                }
            }
        })

    }

    override fun getItemCount(): Int =modelList.size
}

class PriceViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

