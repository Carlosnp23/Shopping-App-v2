package com.example.shoppingappv2

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.shoppingappv2.Services.CustomAdapter
import com.example.shoppingappv2.Services.ItemsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview

class ProductsHomeActivity : AppCompatActivity() {

    private val data1 = ArrayList<ItemsViewModel>()
    private val data2 = ArrayList<ItemsViewModel>()
    private val db = Firebase.firestore
    private lateinit var productsHomeScrollView: ScrollView
    private lateinit var productList1: CarouselRecyclerview
    private lateinit var productList2: RecyclerView
    private lateinit var snapHelper: SnapHelper
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_home)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        progressBar = findViewById(R.id.progressBar)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_cart -> {
                    Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CartActivity::class.java)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
                    startActivity(intent)
                    finish()
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

        productsHomeScrollView = findViewById(R.id.products_home_scroll_view)
        productList1 = findViewById(R.id.product_list_1)
        productList2 = findViewById(R.id.product_list_2)

        // this creates a vertical layout Manager
        object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        productList1.getCarouselLayoutManager().centerPosition()
        productList2.layoutManager = LinearLayoutManager(this)

        productList1.apply {
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
            snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(productList1)
        }
        productList2.apply {
            layoutManager = GridLayoutManager(this.context, 2)
        }
        getProductList1()

        var swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener {
            finish();
            startActivity(intent);
        }
    }

    private fun getProductList1() {
        db.collection("Products")
            .whereEqualTo("Category", "Offer")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "Offer Name => ${document.data["Name"]}")
                    data1.add(
                        ItemsViewModel(
                            document.data["Image"].toString(),
                            document.data["Name"].toString(),
                            document.data["Price"].toString()
                        )
                    )
                }
                getProductList2()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                Toast.makeText(this, "Offer product loading failed", Toast.LENGTH_SHORT).show()
                getProductList2()
            }
    }

    private fun getProductList2() {
        db.collection("Products")
            .whereEqualTo("Category", "No Offer")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "Product Name => ${document.data["Name"]}")
                    data2.add(
                        ItemsViewModel(
                            document.data["Image"].toString(),
                            document.data["Name"].toString(),
                            document.data["Price"].toString()
                        )
                    )
                }
                loadProducts()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                Toast.makeText(this, "All products loading failed", Toast.LENGTH_SHORT).show()
                loadProducts()
            }
    }

    private fun loadProducts() {
        if (data1.isEmpty() && data2.isEmpty()) {
            Toast.makeText(this, "Product list is empty", Toast.LENGTH_SHORT).show()
        } else {
            // This will pass the ArrayList to our Adapter
            val adapter1 = CustomAdapter(data1, this)
            val adapter2 = CustomAdapter(data2, this)

            // Setting the Adapter with the recyclerview
            productList1.adapter = adapter1
            productList2.adapter = adapter2

            //products_home_scroll_view.fullScroll(ScrollView.FOCUS_UP)
            productsHomeScrollView.smoothScrollTo(0, 0)
        }
    }
}