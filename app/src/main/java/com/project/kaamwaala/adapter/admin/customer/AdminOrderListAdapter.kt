package com.project.kaamwaala.adapter.admin.customer


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.admin.AdminOrderDetailActivity
import com.project.kaamwaala.model.admin_order.StoreOrder
import kotlinx.android.synthetic.main.inflate_admin_order_list.view.*


class AdminOrderListAdapter(
    private var modelList: List<StoreOrder>

) :
    RecyclerView.Adapter<AdminOrderViewHolder>() {

    var cartvalue:Int=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        return AdminOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_admin_order_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {

        holder.itemView.cardview.setOnClickListener { holder.itemView.context.startActivity(Intent(holder.itemView.context,AdminOrderDetailActivity::class.java)
            .putExtra("order_id",modelList.get(position).OrderId)
            .putExtra("shipping_charge",modelList.get(position).ShippingCharge)
        )}

        holder.itemView.tvOrderid.setText(modelList.get(position).OrderId)
        holder.itemView.tvReceiveDate.setText("Received"+modelList.get(position).OrderDate)
        holder.itemView.tvStallName.setText(modelList.get(position).StoreName)
        holder.itemView.tvDeliveryDate.setText("Delivery On"+modelList.get(position).DeliveryTime)
        holder.itemView.tvStatus.setText(modelList.get(position).DeliveryStatus)
        holder.itemView.tvTotalAmount.setText(modelList.get(position).GrandTotal)

    }

    override fun getItemCount(): Int =modelList.size
}

class AdminOrderViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

