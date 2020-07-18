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


class ProductImageListAdapter(
    private var modelList: List<ProductImagesItem>,
    private var OnItemClickImage: OnItemClickImage,
    private var OnItemDeleteImage: OnItemDeleteImage
) :
    RecyclerView.Adapter<ProductImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        return ProductImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_product_images
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {

        Glide.with(holder.itemView.context) //1
            .load(modelList.get(position).ImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.itemView.ivProduct)

        holder.itemView.ivProduct.setOnClickListener { OnItemClickImage.onClickImageClick(modelList.get(position).ImagePath,modelList.get(position).Code) }



    }

    override fun getItemCount(): Int =modelList.size

}

class ProductImageViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

