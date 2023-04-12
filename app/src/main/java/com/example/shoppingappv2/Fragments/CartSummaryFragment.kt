package com.example.shoppingappv2.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shoppingappv2.R
import com.example.shoppingappv2.Services.SharedPreference

class CartSummaryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_cart_summary, container, false)

        val sp = SharedPreference(this.context)
        var cartTotalValue = sp.getPreference("cart_total")
        var cartSubtotal = view.findViewById<TextView>(R.id.cart_subtotal)
        var cartTotal = view.findViewById<TextView>(R.id.cart_total)

        if(cartTotalValue != null){
            var cartTotalValue2 = cartTotalValue.toFloat()
            cartSubtotal.text = "CAD $"+String.format("%.2f", cartTotalValue2).toString()
            cartTotal.text = "CAD $"+("%.2f".format((cartTotalValue2+50))).toString()
        }

        return view
    }
}