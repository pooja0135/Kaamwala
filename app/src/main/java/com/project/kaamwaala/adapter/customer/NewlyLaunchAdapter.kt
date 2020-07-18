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
import kotlinx.android.synthetic.main.inflate_new_launch.view.*


class NewlyLaunchAdapter(
    private var modelList: List<NewLaunched>
) :
    RecyclerView.Adapter<NewlyLaunchViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewlyLaunchViewHolder {
        return NewlyLaunchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_new_launch
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NewlyLaunchViewHolder, position: Int) {

        Glide.with(holder.itemView.context) //1
            .asBitmap()
            .load(modelList.get(position).StoreImage)
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


        holder.itemView.tvCategory.text=modelList.get(position).CategoryName
        holder.itemView.tvName.text=modelList.get(position).StoreName
    }

    override fun getItemCount(): Int =modelList.size
}

class NewlyLaunchViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

