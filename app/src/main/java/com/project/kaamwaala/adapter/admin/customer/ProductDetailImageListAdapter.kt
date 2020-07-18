package com.project.kaamwaala.adapter.admin.ProductImage


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.kaamwaala.*
import com.project.kaamwaala.model.stall_category_product.ProductImagesItem
import kotlinx.android.synthetic.main.inflate_admin_product_list.view.*
import kotlinx.android.synthetic.main.product_image_dialog.view.*


class ProductDetailImageListAdapter(
    private var modelList: List<ProductImagesItem>
) :
    RecyclerView.Adapter<ProductDetailImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailImageViewHolder {
        return ProductDetailImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_product_detail_images
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductDetailImageViewHolder, position: Int) {

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivProduct)




    }

    override fun getItemCount(): Int =modelList.size

}

class ProductDetailImageViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

