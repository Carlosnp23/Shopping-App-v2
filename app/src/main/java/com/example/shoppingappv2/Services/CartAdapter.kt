package com.example.shoppingappv2.Services

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingappv2.R
import com.squareup.picasso.Picasso

class CartAdapter(val context: Context) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    lateinit var sp: SharedPreference
    lateinit var cartList: Array<String>

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_card_view, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cartItemString = cartList[position]
        try {
            var cartItem = cartItemString.split("^").toTypedArray()

            Picasso.get().load(cartItem[0]).into(holder.imageView);
            holder.productName.text = cartItem[1]
            holder.productPrice.text = "CAD $" + cartItem[2]
        } catch (e: Exception) {
            Log.e("Cart item exception", e.toString())
            Log.e("Cart item exception", cartItemString)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        sp = SharedPreference(context)
        var cartListString = sp.getPreference("cart_list")

        if (cartListString == null) {
            return 0
        } else {
            cartList = cartListString.split("|").toTypedArray()
            return cartList.size
        }
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cart_product_image)
        val productName: TextView = itemView.findViewById(R.id.cart_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.cart_product_price)
    }
}