package com.example.shoppingappv2.Services

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingappv2.R
import com.squareup.picasso.Picasso

class CustomAdapter(private val mList: List<ItemsViewModel>, val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    lateinit var sp: SharedPreference

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.products_home_card_view, parent, false)

        sp = SharedPreference(context)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        Log.i(
            "Items",
            position.toString() + " | " + itemsViewModel.Name + " | " + itemsViewModel.Price
        )
        Picasso.get().load(itemsViewModel.Image).into(holder.imageView);
        holder.productName.text = itemsViewModel.Name
        holder.productPrice.text = "CAD $" + itemsViewModel.Price
        holder.addToCart.setOnClickListener(View.OnClickListener {
            var cartList = sp.getPreference("cart_list")
            if (cartList == null) {
                cartList =
                    itemsViewModel.Image + "^" + itemsViewModel.Name + "^" + itemsViewModel.Price
            } else {
                cartList += "|" + itemsViewModel.Image + "^" + itemsViewModel.Name + "^" + itemsViewModel.Price
            }
            sp.setPreference("cart_list", cartList)
            var cartTotalValueString = sp.getPreference("cart_total")
            var cartTotalValue: Float
            var price =
                String.format("%.2f", ((itemsViewModel.Price).replace(",", "")).toFloat()).toFloat()
            if (cartTotalValueString == null) {
                cartTotalValue = price
            } else {
                cartTotalValue =
                    String.format("%.2f", cartTotalValueString.toFloat()).toFloat()
                cartTotalValue += price
            }
            sp.setPreference("cart_total", cartTotalValue.toString())
            Toast.makeText(context, "Item added to cart!", Toast.LENGTH_SHORT).show()
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val addToCart: ImageView = itemView.findViewById(R.id.add_to_cart)
    }
}