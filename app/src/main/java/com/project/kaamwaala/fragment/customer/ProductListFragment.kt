package com.project.kaamwaala.fragment.customer


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.ProductListAdapter
import com.project.kaamwaala.OnItemAdd
import com.project.kaamwaala.OnItemMinus
import com.project.kaamwaala.activity.customer.product.CartListActivity
import com.project.kaamwaala.activity.customer.product.CustomerProductListVM
import com.project.kaamwaala.activity.customer.product.CustomerProductListVMF
import com.project.kaamwaala.model.customer.cart.*
import com.project.kaamwaala.model.customer.customer_product.CustomerProductRequest
import com.project.kaamwaala.model.customer.customer_product.CustomerProductResponse
import com.project.kaamwaala.network.PrefsConstants
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.progressbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ProductListFragment: Fragment(), OnItemAdd, OnItemMinus, KodeinAware {
    override val kodein by kodein()
    private val factory: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private var viewModel: CustomerProductListVM? = null

    var list: MutableList<String> = mutableListOf()
    var listitem: MutableList<CartItem> = mutableListOf()
    var cartprice:Double=0.0
    private lateinit var OnItemAdd: OnItemAdd
    private lateinit var OnItemMinus: OnItemMinus

    fun newInstance(id: String): ProductListFragment {
        val args = Bundle()
        args.putString("id", id)
        val fragment = ProductListFragment()
        fragment.arguments = args

        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View=inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(CustomerProductListVM::class.java)

        OnItemAdd=this
        OnItemMinus=this

        try{
            listitem = Paper.book().read("cartdetail")
        }
        catch(e:Exception)
        {

        }



        return  view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (listitem.size>0)
        {
            tvCount.visibility=View.VISIBLE
            tvCount.setText(listitem.size.toString())

            for (i in 0 until listitem.size)
            {
                if(listitem.get(i).SalePrice.isEmpty())
                {
                   cartprice=cartprice+(listitem.get(i).Price.toDouble()*listitem.get(i).Quantity.toInt())
                }
                else
                {
                    cartprice=cartprice+(listitem.get(i).SalePrice.toDouble()*listitem.get(i).Quantity.toInt())
                }
            }
            rlPrice.visibility=View.VISIBLE
            tvTotalPrice.setText("\u20B9"+cartprice.toString())

        }
        else
        {
            tvCount.visibility=View.GONE
        }






            rlCart.setOnClickListener {
            startActivity(Intent(activity, CartListActivity::class.java))
        }

    }

  //=============================API===========================================================//
    private fun GetProduct(customercategoryRequest: CustomerProductRequest) {
        viewModel?.responseProduct = MutableLiveData()
        viewModel?.responseProduct?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CustomerProductResponse)

            if (response.Status) {
                rvHome.apply {
                    rvHome.layoutManager = LinearLayoutManager(context)
                    adapter = ProductListAdapter(response.CustomerProductItem,OnItemAdd,OnItemMinus) }


            } else {

                //  activity?.toast("Something went wrong.")
            }
        })

        progressbar.visibility=View.GONE

        viewModel?.getProduct(customercategoryRequest)
    }
    private fun AddCartApi(addCartRequest: AddCartRequest) {
        viewModel?.responseAddCart= MutableLiveData()
        viewModel?.responseAddCart?.observe(this, androidx.lifecycle.Observer {

            val response = (it as AddCartResponse)
            if (response.Status) {
                tvCount.text=response.CartCount
                rlPrice.visibility=View.VISIBLE
                progressbar.visibility= View.GONE
            }
            else
            {
                progressbar.visibility= View.GONE
            }

            GetCart(GetCartRequest("Select", Prefs.getString(PrefsConstants.CityId,""),"","",Prefs.getString(PrefsConstants.UserId,""),Prefs.getString(PrefsConstants.StoreId,"")))


        })
        viewModel?.addcart(addCartRequest)
    }

    private fun GetCart(getCartRequest: GetCartRequest) {
        viewModel?.responseGetCart = MutableLiveData()
        viewModel?.responseGetCart?.observe(this, androidx.lifecycle.Observer {

            val response = (it as GetCartResponse)

            Paper.book().delete("cartdetail")
            if (response.Status) {


                Paper.book()
                    .write("cartdetail", response.CartItem)

                tvCount.visibility= View.VISIBLE
                tvCount.setText(response.CartItem.size.toString())

            } else {

                //  activity?.toast("Something went wrong.")
            }


        })

        viewModel?.getcart(getCartRequest)
    }

    override fun onResume() {
        super.onResume()
        progressbar.visibility=View.VISIBLE
        GetProduct(CustomerProductRequest("ProductList", Prefs.getString(PrefsConstants.CityId,""),"",
            arguments!!.getString("id").toString(),""))

    }

    override fun onClickAdd(id: String, price: String,product_id:String ,quantity:String) {

        AddCartApi(AddCartRequest("Insert",Prefs.getString(PrefsConstants.UserId,""),id,product_id,quantity,Prefs.getString(PrefsConstants.StoreId,"")))
        cartprice=cartprice+price.toDouble()
        tvTotalPrice.text="\u20B9"+cartprice
    }

    override fun onClickMinus(id: String, price: String,value:String,product_id:String,quantity:String,action:String) {
        AddCartApi(AddCartRequest("Update",Prefs.getString(PrefsConstants.UserId,""),id,product_id,quantity,Prefs.getString(PrefsConstants.StoreId,"")))

        if (action.equals("Add"))
        {
            cartprice=cartprice+price.toDouble()
        }
        else
        {
            cartprice=cartprice-price.toDouble()
        }


        tvTotalPrice.text="\u20B9"+cartprice
    }

}

