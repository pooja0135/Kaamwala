package com.project.kaamwaala.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.OnItemMinus
import com.project.kaamwaala.OnSelectAddress
import com.project.kaamwaala.R
import com.project.kaamwaala.model.customer.cart.CustomerAddres
import kotlinx.android.synthetic.main.inflate_address_list.view.*


class AddressListAdapter(

    private var modelList: List<CustomerAddres>,
    private var OnSelectAddress: OnSelectAddress
) :
    RecyclerView.Adapter<AddressViewHolder>() {

    var selectedposition:Int=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_address_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        if (selectedposition==position)
        {
            holder.itemView.cardview.setCardBackgroundColor(holder.itemView.context.resources.getColor(R.color.grey_400))
        }
        else
        {
            holder.itemView.cardview.setCardBackgroundColor(holder.itemView.context.resources.getColor(R.color.white))
        }

        holder.itemView.tvAddress.setText(modelList.get(position).Address)

        holder.itemView.cardview.setOnClickListener {

            OnSelectAddress.onClickAddress(modelList.get(position).AddressId)
            selectedposition=position
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int =modelList.size
}

class AddressViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

