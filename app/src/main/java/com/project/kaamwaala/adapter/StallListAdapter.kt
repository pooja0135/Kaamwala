package com.project.kaamwaala.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.category.ProductCategoryActivity
import com.project.kaamwaala.model.customer.customer_store.Store
import com.project.kaamwaala.network.PrefsConstants
import kotlinx.android.synthetic.main.inflate_stall_list.view.*


class StallListAdapter(
    private var modelList: List<Store>
) :
    RecyclerView.Adapter<StallViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StallViewHolder {
        return StallViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_stall_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StallViewHolder, position: Int) {

        holder.itemView.cardviewStall.setOnClickListener {
            Prefs.putString(PrefsConstants.StoreId,modelList.get(position).StoreId)

            holder.itemView.context.startActivity(Intent( holder.itemView.context,
            ProductCategoryActivity::class.java).putExtra("store_id",modelList.get(position).StoreId))
        }

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).StoreImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.imageview)

        holder.itemView.tvCategory.setText(modelList.get(position).StoreTagLine)
        holder.itemView.tvStallName.setText(modelList.get(position).StoreName)
        holder.itemView.tvRating.setText(modelList.get(position).Rating)


        if (modelList.get(position).Rating.isEmpty())
        {
            holder.itemView.tvRating.visibility=View.GONE
        }
        else
        {  holder.itemView.tvRating.visibility=View.VISIBLE
            holder.itemView.tvRating.setText(modelList.get(position).Rating)
        }

        if (modelList.get(position).Votes.isEmpty())
        {
            holder.itemView.llvotes.visibility=View.GONE
        }
        else
        {
            holder.itemView.llvotes.visibility=View.VISIBLE
            holder.itemView.tvCount.setText(modelList.get(position).Votes +" votes")
        }



        if (modelList.get(position).StockMinOrder.isEmpty())
        {
            holder.itemView.tvStoreMinimumOrder.visibility=View.GONE
        }
        else
        {
            holder.itemView.tvStoreMinimumOrder.setText("Min Order \u20B9"+modelList.get(position).StockMinOrder)
        }

        if (modelList.get(position).DiscountPercent.isEmpty())
        {
            holder.itemView.llDiscount.visibility=View.GONE
        }
        else
        {
            holder.itemView.llDiscount.visibility=View.VISIBLE
            if (modelList.get(position).OfferType=="Flat")
            {
                holder.itemView.tvUpto.visibility=View.GONE

            }
            else
            {
                holder.itemView.tvUpto.setText("Upto \u20B9"+modelList.get(position).MaxAmount)
            }
            holder.itemView.tvOffer.setText(modelList.get(position).DiscountPercent+"% OFF")
            holder.itemView.tvMinOrder.setText(",Min Order \u20B9"+modelList.get(position).MinAmount)
        }

    }

    override fun getItemCount(): Int =modelList.size
}

class StallViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

