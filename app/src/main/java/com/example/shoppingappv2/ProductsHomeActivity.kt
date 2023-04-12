package com.example.shoppingappv2

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.shoppingappv2.Services.CustomAdapter
import com.example.shoppingappv2.Services.ItemsViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductsHomeActivity : AppCompatActivity() {

    private val data1 = ArrayList<ItemsViewModel>()
    private val data2 = ArrayList<ItemsViewModel>()
    private val db = Firebase.firestore
    lateinit var productsHomeScrollView: ScrollView
    lateinit var productList1: RecyclerView
    lateinit var productList2: RecyclerView
    //lateinit var popAlert: Pop_Alert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPage = "Home"
        setContentView(R.layout.activity_products_home)

        //popAlert = Pop_Alert(this, this)

        productsHomeScrollView = findViewById<ScrollView>(R.id.products_home_scroll_view)
        productList1 = findViewById<RecyclerView>(R.id.product_list_1)
        productList2 = findViewById<RecyclerView>(R.id.product_list_2)

        // this creates a vertical layout Manager
        object : LinearLayoutManager(this){ override fun canScrollVertically(): Boolean { return false } }
        productList1.layoutManager = LinearLayoutManager(this)
        productList2.layoutManager = LinearLayoutManager(this)

        productList1.apply {
            layoutManager = GridLayoutManager(this.context, 2)
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

    private fun getProductList1(){
        db.collection("Products")
            .whereEqualTo("Category", "Offer")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "Offer Name => ${document.data["Name"]}")
                    data1.add(ItemsViewModel(document.data["Image"].toString(), document.data["Name"].toString(), document.data["Price"].toString()))
                }
                getProductList2()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                Toast.makeText(this, "Offer product loading failed", Toast.LENGTH_SHORT).show()
                getProductList2()
            }
    }

    private fun getProductList2(){
        db.collection("Products")
            .whereEqualTo("Category", "No Offer")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "Product Name => ${document.data["Name"]}")
                    data2.add(ItemsViewModel(document.data["Image"].toString(), document.data["Name"].toString(), document.data["Price"].toString()))
                }
                loadProducts()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                Toast.makeText(this, "All products loading failed", Toast.LENGTH_SHORT).show()
                loadProducts()
            }
    }

    private fun loadProducts(){
        if(data1.isEmpty() && data2.isEmpty()){
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