package com.project.kaamwaala.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R



class DeliveryListAdapter(
    private var modelList: List<String>
) :
    RecyclerView.Adapter<DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        return DeliveryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_delivery_type_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {


    }

    override fun getItemCount(): Int =3
}

class DeliveryViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

