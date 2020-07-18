package com.project.kaamwaala.adapter.customer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.project.kaamwaala.R
import com.project.kaamwaala.model.customer.dashboard.Banner
import kotlinx.android.synthetic.main.activity_admin_category_detail.*
import java.util.*


 class CardPagerAdapter( val mContext: Context, var arraylist: List<Banner>) :
    PagerAdapter(), CardAdapter {
    private var mBaseElevation = 0f



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.viewpager, container, false)
        container.addView(view)
        val cardView = view.findViewById<View>(R.id.cardView) as CardView
        val iv =
            view.findViewById<View>(R.id.image) as ImageView
        if (mBaseElevation == 0.0f) {
            mBaseElevation = cardView.cardElevation
        }


        Glide.with(mContext) //1
            .asBitmap()
            .load(arraylist.get(position).ImageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    iv.setImageBitmap(resource)

                }
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)


                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })


        cardView.maxCardElevation = mBaseElevation * arraylist.size.toFloat()

        return view
    }

    override fun destroyItem(
        collection: ViewGroup,
        position: Int,
        view: Any
    ) {
        collection.removeView(view as View)
    }

     override fun getCardViewAt(i: Int): CardView {
         TODO("Not yet implemented")
     }

     override fun getBaseElevation(): Float {
         return 0.0F
     }


     override fun getCount(): Int {
        return arraylist.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }




}
