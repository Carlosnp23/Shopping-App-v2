package com.example.shoppingappv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingappv2.Services.CartAdapter
import com.example.shoppingappv2.Services.SharedPreference
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPage = "Cart"
        setContentView(R.layout.activity_cart)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProductsHomeActivity::class.java)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.action_cart -> {
                    Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }

        val sp = SharedPreference(this)

        var cart_clear_cart = findViewById<Button>(R.id.cart_clear_cart)
        var cart_checkout = findViewById<Button>(R.id.cart_checkout)
        var cart_summary = findViewById<ConstraintLayout>(R.id.cart_summary)

        var cart_list_string = sp.getPreference("cart_list")
        var cart_total_value_string = sp.getPreference("cart_total")

        if(cart_list_string == null || cart_total_value_string == null){
            cart_checkout.isEnabled = false
            cart_summary.visibility = View.GONE
            Toast.makeText(this, "No items in the cart!", Toast.LENGTH_SHORT).show()
        } else {
            val cart_list_view = findViewById<RecyclerView>(R.id.cart_list_view)
            cart_list_view.layoutManager = LinearLayoutManager(this)
            val cart_list_adapter = CartAdapter(this)
            cart_list_view.adapter = cart_list_adapter

            cart_checkout.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            })
        }
        cart_clear_cart.setOnClickListener(View.OnClickListener {
            sp.removePreference("cart_list")
            sp.removePreference("cart_total")
            val intent = Intent(this, ProductsHomeActivity::class.java)
            startActivity(intent)
        })
    }
}