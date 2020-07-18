package com.project.kaamwaala.adapter


import android.content.Intent
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
import com.project.kaamwaala.activity.customer.product.ProductListActivity
import com.project.kaamwaala.model.customer.customer_category.StoreCategoryItem
import kotlinx.android.synthetic.main.inflate_stall_category_list.view.*
import kotlinx.android.synthetic.main.inflate_stall_list.view.cardviewStall


class StallCategoryListAdapter(
    private var modelList: List<StoreCategoryItem>
) :
    RecyclerView.Adapter<StallCategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StallCategoryViewHolder {
        return StallCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_stall_category_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StallCategoryViewHolder, position: Int) {

        holder.itemView.cardviewStall.setOnClickListener { holder.itemView.context.startActivity(Intent( holder.itemView.context,
            ProductListActivity::class.java).putExtra("category_id",modelList.get(position).StoreCategoryId).putExtra("position",position.toString())) }

        Glide.with(holder.itemView.context) //1
            .asBitmap()
            .load(modelList.get(position).StoreCategoryImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    holder.itemView.ivCategory.setImageBitmap(resource)

                }
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)


                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })


        holder.itemView.tvCategoryName.setText(modelList.get(position).StoreCategoryName)

    }

    override fun getItemCount(): Int =modelList.size
}

class StallCategoryViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

