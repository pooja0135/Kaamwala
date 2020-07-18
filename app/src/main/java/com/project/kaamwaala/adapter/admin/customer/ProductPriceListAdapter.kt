package com.project.kaamwaala.adapter.admin.customer


import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.OnItemAdd
import com.project.kaamwaala.OnItemMinus
import com.project.kaamwaala.model.adminstalllist.StoreItem
import com.project.kaamwaala.model.customer.cart.CartItem
import com.project.kaamwaala.model.customer.customer_product.ProductPriceItem
import io.paperdb.Paper
import kotlinx.android.synthetic.main.inflate_cart_list.view.cardviewPlus
import kotlinx.android.synthetic.main.inflate_price_list.view.*
import kotlinx.android.synthetic.main.inflate_product_price_list.view.*
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvDiscount
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvPrice
import kotlinx.android.synthetic.main.inflate_product_price_list.view.tvSalePrice


class ProductPriceListAdapter(
    private var modelList: List<ProductPriceItem>,
    private var OnItemAdd: OnItemAdd,
    private var OnItemMinus: OnItemMinus
) :
    RecyclerView.Adapter<ProductPriceViewHolder>() {

    var cartvalue:Int=0
    var listitem: MutableList<CartItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPriceViewHolder {
        return ProductPriceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_product_price_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductPriceViewHolder, position: Int) {

        try{
            listitem = Paper.book().read("cartdetail")
        }
        catch(e:Exception)
        {

        }


        Log.v("fhhfjhfjfhfh",listitem.size.toString())



        for (i in 0 until listitem.size) {

            if (listitem.get(i).ProductId.equals(modelList.get(position).ProductId)&&listitem.get(i).PriceItemId.equals(modelList.get(position).Id))
            {


                holder.itemView.tvValue.text=listitem.get(i).Quantity
                holder.itemView.cardviewPlus.visibility=View.VISIBLE
                holder.itemView.cardviewAdd.visibility=View.GONE
            }
        }

        holder.itemView.cardviewAdd.setOnClickListener {
            holder.itemView.cardviewPlus.visibility=View.VISIBLE
            holder.itemView.cardviewAdd.visibility=View.GONE
            var cartvalue:Int=0
            cartvalue=holder.itemView.tvValue.text.toString().toInt()
            cartvalue=cartvalue+1
            holder.itemView.tvValue.setText(cartvalue.toString())
            if (modelList.get(position).SalePrice.isEmpty())
            {
                OnItemAdd.onClickAdd(modelList.get(position).Id, modelList.get(position).Price,modelList.get(position).ProductId,holder.itemView.tvValue.text.toString())

            }
            else
            {
                OnItemAdd.onClickAdd(modelList.get(position).Id, modelList.get(position).SalePrice,modelList.get(position).ProductId,holder.itemView.tvValue.text.toString())

            }
        }

        holder.itemView.rlAdd.setOnClickListener {
            var cartvalue:Int=0
            cartvalue=holder.itemView.tvValue.text.toString().toInt()
            cartvalue = cartvalue + 1
            holder.itemView.tvValue.setText(cartvalue.toString())


            if (modelList.get(position).SalePrice.isEmpty()) {
                OnItemMinus.onClickMinus(modelList.get(position).Id, modelList.get(position).Price,holder.itemView.tvValue.text.toString(),modelList.get(position).ProductId,holder.itemView.tvValue.text.toString(),"Add")

            } else {
                OnItemMinus.onClickMinus(modelList.get(position).Id, modelList.get(position).SalePrice,holder.itemView.tvValue.text.toString(),modelList.get(position).ProductId,holder.itemView.tvValue.text.toString(),"Add")

            }

        }
        holder.itemView.rlMinus.setOnClickListener {
                var cartvalue:Int=0
                cartvalue=holder.itemView.tvValue.text.toString().toInt()
                cartvalue = cartvalue - 1
                holder.itemView.tvValue.setText(cartvalue.toString())
                if (modelList.get(position).SalePrice.isEmpty())
                {
                    OnItemMinus.onClickMinus(modelList.get(position).Id, modelList.get(position).Price,holder.itemView.tvValue.text.toString(),modelList.get(position).ProductId,holder.itemView.tvValue.text.toString(),"Minus")

                }
                else
                {
                    OnItemMinus.onClickMinus(modelList.get(position).Id, modelList.get(position).SalePrice,holder.itemView.tvValue.text.toString(),modelList.get(position).ProductId,holder.itemView.tvValue.text.toString(),"Minus")

                }
                if (cartvalue == 0) {
                    holder.itemView.cardviewPlus.visibility = View.GONE
                    holder.itemView.cardviewAdd.visibility = View.VISIBLE
                }

        }


        holder.itemView.tvPrice.setText("\u20B9"+modelList.get(position).Price)
        holder.itemView.tvSalePrice.setText("\u20B9"+modelList.get(position).SalePrice)

        holder.itemView.tvDescription.setText(modelList.get(position).Describe)



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

    }

    override fun getItemCount(): Int =modelList.size
}

class ProductPriceViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

