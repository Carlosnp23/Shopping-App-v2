package com.example.shoppingappv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingappv2.Services.CartAdapter
import com.example.shoppingappv2.Services.SharedPreference
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CartActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        progressBar = findViewById(R.id.progressBar)

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
                R.id.action_exit -> {
                    Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.VISIBLE
                    FirebaseAuth.getInstance().signOut();
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }

        val sp = SharedPreference(this)

        var cartClearCart = findViewById<Button>(R.id.cart_clear_cart)
        var cartCheckout = findViewById<Button>(R.id.cart_checkout)
        var cartSummary = findViewById<ConstraintLayout>(R.id.cart_summary)

        var cartList = sp.getPreference("cart_list")
        var cartTotalValue = sp.getPreference("cart_total")

        if(cartList == null || cartTotalValue == null){
            cartCheckout.isEnabled = false
            cartSummary.visibility = View.GONE
            Toast.makeText(this, "No items in the cart!", Toast.LENGTH_SHORT).show()
        } else {
            val cartListView = findViewById<RecyclerView>(R.id.cart_list_view)
            cartListView.layoutManager = LinearLayoutManager(this)
            val cartListAdapter = CartAdapter(this)
            cartListView.adapter = cartListAdapter

            cartCheckout.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            })
        }
        cartClearCart.setOnClickListener(View.OnClickListener {
            sp.removePreference("cart_list")
            sp.removePreference("cart_total")
            val intent = Intent(this, ProductsHomeActivity::class.java)
            startActivity(intent)
        })
    }
}