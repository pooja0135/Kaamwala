package com.project.kaamwaala.adapter


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.OnItemAdd
import com.project.kaamwaala.OnItemMinus
import com.project.kaamwaala.R
import com.project.kaamwaala.model.customer.cart.CartItem
import kotlinx.android.synthetic.main.inflate_cart_list.view.*
import kotlinx.android.synthetic.main.inflate_cart_list.view.cardviewPlus
import kotlinx.android.synthetic.main.inflate_cart_list.view.rlAdd
import kotlinx.android.synthetic.main.inflate_cart_list.view.rlMinus
import kotlinx.android.synthetic.main.inflate_cart_list.view.tvDiscount
import kotlinx.android.synthetic.main.inflate_cart_list.view.tvPrice
import kotlinx.android.synthetic.main.inflate_cart_list.view.tvSalePrice


class CartListAdapter(
    private var modelList: List<CartItem>,
    private var OnItemMinus: OnItemMinus
) :
    RecyclerView.Adapter<CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_cart_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivProduct)

        holder.itemView.tvPrice.setText("\u20B9"+modelList.get(position).Price)
        holder.itemView.tvSalePrice.setText("\u20B9"+modelList.get(position).SalePrice)

        holder.itemView.tvDescription.setText(modelList.get(position).Describe)
        holder.itemView.tvProductName.setText(modelList.get(position).ProductName)
        holder.itemView.tvQuantity.setText(modelList.get(position).Quantity)



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
        holder.itemView.rlAdd.setOnClickListener {
            var cartvalue:Int=0
            cartvalue=holder.itemView.tvQuantity.text.toString().toInt()
            cartvalue = cartvalue + 1
            holder.itemView.tvQuantity.setText(cartvalue.toString())


            if (modelList.get(position).SalePrice.isEmpty()) {
                OnItemMinus.onClickMinus(modelList.get(position).PriceItemId, modelList.get(position).Price,holder.itemView.tvQuantity.text.toString(),modelList.get(position).ProductId,holder.itemView.tvQuantity.text.toString(),"Add")

            } else {
                OnItemMinus.onClickMinus(modelList.get(position).PriceItemId, modelList.get(position).SalePrice,holder.itemView.tvQuantity.text.toString(),modelList.get(position).ProductId,holder.itemView.tvQuantity.text.toString(),"Add")

            }

        }
        holder.itemView.rlMinus.setOnClickListener {
            var cartvalue:Int=0
            cartvalue=holder.itemView.tvQuantity.text.toString().toInt()
            cartvalue = cartvalue - 1
            holder.itemView.tvQuantity.setText(cartvalue.toString())
            if (modelList.get(position).SalePrice.isEmpty())
            {
                OnItemMinus.onClickMinus(modelList.get(position).PriceItemId, modelList.get(position).Price,holder.itemView.tvQuantity.text.toString(),modelList.get(position).ProductId,holder.itemView.tvQuantity.text.toString(),"Minus")

            }
            else
            {
                OnItemMinus.onClickMinus(modelList.get(position).PriceItemId, modelList.get(position).SalePrice,holder.itemView.tvQuantity.text.toString(),modelList.get(position).ProductId,holder.itemView.tvQuantity.text.toString(),"Minus")

            }
            if (cartvalue == 0) {
                holder.itemView.cardviewPlus.visibility = View.GONE

            }

        }


    }

    override fun getItemCount(): Int =modelList.size
}

class CartViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

