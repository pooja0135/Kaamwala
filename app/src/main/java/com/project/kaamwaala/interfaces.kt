package com.project.kaamwaala
import com.project.kaamwaala.model.LocalAddressModel
import com.project.kaamwaala.model.stall_category_product.ProductImagesItem
import org.intellij.lang.annotations.JdkConstants

interface OnItemClickSetting {
    fun onClick(settingtype: String,value:String)
}

interface OnItemClickStall {
    fun onClickStall(storeid: String)
}

interface OnItemClickCreateStall {
    fun onClickCreateStall(storename: String)
}

interface ForgetPasswordResultListener {
    fun onResult(message: String)
}

interface OnItemDelete {
    fun onClickDelete(id: String,position:Int)
}

interface OnItemDeleteFreeShipping {
    fun onClickFreeShipping(id: String,position:Int)
}

interface OnItemDeleteShipping {
    fun onClickShipping(id: String,position:Int)
}

interface OnItemDeleteTime {
    fun onClickTime(id: String,position:Int)
}

interface OnItemCreateCategory{
    fun onClickCategory(id: String)
}

interface OnAddImageUrl{
    fun onClickImage(url: String)
}

interface OnItemCreateProduct{
    fun onClickProduct(name: String)
}

interface OnItemProductPrice{
    fun onClickProductPrice(price: String,saleprice:String,describe:String,action:String,id:String)
}

interface OnItemEditPrice{
    fun onClickEditPrice(price: String,saleprice:String,describe:String,id:String)
}

interface OnItemDeletePrice{
    fun onClickDeletePrice(id:String)
}

interface OnItemStock{
    fun onClickStock(id:String,value:String,price:String)
}

interface OnItemClickImage{
    fun onClickImageClick(id: String,url:String)
}

interface OnItemDeleteImage{
    fun onClickDeleteImage(id:String,productid:String,imageurl:String)
}

interface OnItemAdd{
    fun onClickAdd(id:String,price:String,product_id:String,quantity:String)
}

interface OnItemMinus{
    fun onClickMinus(id:String,price:String,value:String,product_id:String,quantity:String,action:String)
}




interface OnItemSelectCategory{
    fun onClickCategory(id:String,name:String)
}

interface OnItemSelect{
    fun onClick(id:String,name:String)
}

interface OnItemSelectCity{
    fun onClickCity(id:String,name:String)
}

interface OnItemProductDescription{
    fun onClickDescription(value:String)
}

interface OnSelectAddress{
    fun onClickAddress(id:String)
}

interface OnclickAddress {
    fun onClickAddress( a : LocalAddressModel)
}
