package com.project.kaamwaala.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.product.OrderDetailActivity
import com.project.kaamwaala.model.customer.order.Order
import kotlinx.android.synthetic.main.inflate_order_list.view.*


class MyOrderListAdapter(
    private var modelList: List<Order>
) :
    RecyclerView.Adapter<MyOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder {
        return MyOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_order_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyOrderViewHolder, position: Int) {

        holder.itemView.cardView.setOnClickListener {
            holder.itemView.context.startActivity(Intent(holder.itemView.context, OrderDetailActivity::class.java)
                .putExtra("order_id",modelList.get(position).OrderId)
                .putExtra("delivery_time",modelList.get(position).DeliveryTime)
                .putExtra("order_date",modelList.get(position).OrderDate)
                .putExtra("shipping_charge",modelList.get(position).ShippingCharge)
                .putExtra("payment_mode",modelList.get(position).PaymentMode)
            )
        }

        holder.itemView.tvOrderId.setText(modelList.get(position).OrderId)
        holder.itemView.tvOrderDate.setText(modelList.get(position).OrderDate)
        holder.itemView.tvStoreName.setText(modelList.get(position).StoreName)
        holder.itemView.tvStatus.setText(modelList.get(position).DeliveryStatus)

        if(modelList.get(position).DeliveryStatus.equals("New"))
        {
            holder.itemView.tvStatus.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.yellow_700))
        }

        if(modelList.get(position).DeliveryStatus.equals("Shipped"))
        {
            holder.itemView.tvStatus.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.yellow_700))
        }

        if(modelList.get(position).DeliveryStatus.equals("Delivered"))
        {
            holder.itemView.tvStatus.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.green_700))
            holder.itemView.tvDeliveryStatus.visibility=View.GONE
        }
        if(modelList.get(position).DeliveryStatus.equals("Cancelled"))
        {
            holder.itemView.tvStatus.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.red_700))
            holder.itemView.tvDeliveryStatus.visibility=View.GONE
        }


        holder.itemView.tvDeliveryStatus.setText("Delivery "+modelList.get(position).DeliveryTime)
        holder.itemView.tvTotalAmount.setText("\u20B9"+modelList.get(position).TotalAmount+"/-")
    }

    override fun getItemCount(): Int =modelList.size
}

class MyOrderViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

