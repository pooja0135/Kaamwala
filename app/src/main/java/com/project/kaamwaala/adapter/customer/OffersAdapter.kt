package com.project.kaamwaala.adapter.customer


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.project.kaamwaala.R
import com.project.kaamwaala.model.customer.dashboard.NewLaunched
import com.project.kaamwaala.model.customer.dashboard.Offer
import kotlinx.android.synthetic.main.inflate_new_launch.view.*
import kotlinx.android.synthetic.main.inflate_new_launch.view.imageview
import kotlinx.android.synthetic.main.inflate_offer_list.view.*


class OffersAdapter(
    private var modelList: List<Offer>
) :
    RecyclerView.Adapter<OffersViewHolder>() {

    var pos=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersViewHolder {
        return OffersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_offer_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {
        pos=position+pos


            Glide.with(holder.itemView.context) //1
                .asBitmap()
                .load(modelList.get(pos).StoreImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        holder.itemView.imageview.setImageBitmap(resource)

                    }
                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)


                    }
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })


            holder.itemView.tvStoreName.text=modelList.get(position).StoreName
            holder.itemView.tvDiscount.text=modelList.get(position).DiscountPer+"% off"


         /*   try{
                Glide.with(holder.itemView.context) //1
                    .asBitmap()
                    .load(modelList.get(pos+1).StoreImage)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .skipMemoryCache(true) //2
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            holder.itemView.imageview1.setImageBitmap(resource)

                        }
                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)


                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })




                holder.itemView.tvStoreName1.text=modelList.get(pos+1).StoreName
                holder.itemView.tvDiscount1.text=modelList.get(pos+1).DiscountPer+"% off"

                pos=pos+1

            }
            catch (e:Exception)
            {
                holder.itemView.linealayout.visibility=View.GONE
            }*/



    }

    override fun getItemCount(): Int =modelList.size
}

class OffersViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

