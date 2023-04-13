package com.example.shoppingappv2

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.shoppingappv2.Services.SharedPreference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckoutActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreference
    private lateinit var username: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var checkoutFeedbackImage: ImageView
    private lateinit var checkoutFeedback: FrameLayout
    private lateinit var paymentOption: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        sp = SharedPreference(this)

        val checkoutName = findViewById<EditText>(R.id.checkout_name)
        val checkoutAddress = findViewById<EditText>(R.id.checkout_address)
        val checkoutPhone = findViewById<EditText>(R.id.checkout_phone)
        val checkoutEmail = findViewById<EditText>(R.id.checkout_email)
        val checkoutBack = findViewById<Button>(R.id.checkout_back)
        val checkoutProceed = findViewById<Button>(R.id.checkout_proceed)
        val checkoutPaymentOptions = findViewById<RadioGroup>(R.id.checkout_payment_options)
        checkoutFeedbackImage = findViewById(R.id.checkout_feedback_image)
        checkoutFeedback = findViewById(R.id.checkout_feedback)

        checkoutEmail.setText(sp.getPreference("user_email"))
        checkoutName.setText(sp.getPreference("user_name"))

        checkoutBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        })

        checkoutProceed.setOnClickListener(View.OnClickListener {
            name = checkoutName.text.toString()
            address = checkoutAddress.text.toString()
            phone = checkoutPhone.text.toString()
            email = checkoutEmail.text.toString()

            if(name.isNotEmpty() && address.isNotEmpty() &&
                phone.isNotEmpty() && email.isNotEmpty()) {

                val selectedId: Int = checkoutPaymentOptions.checkedRadioButtonId
                if (selectedId != -1) {
                    val checkoutPaymentOption =
                        (findViewById<View>(selectedId) as RadioButton).text

                    if (checkoutPaymentOption == "Pay Online") {
                        paymentOption = "Pay Online"
                        Toast.makeText(this, "Pay Online", Toast.LENGTH_SHORT).show()
                    } else {
                        paymentOption = "Cash On Delivery"
                    }
                    saveToDB()
                } else {
                    Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All the fields should be filled properly", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToDB(){
        val cartListString = sp.getPreference("cart_list")
        val cartList = cartListString.split("|").toTypedArray()

        val db = Firebase.firestore
        val orders = db.collection("Orders")

        // Add a new document with a generated id.
        val order = hashMapOf(
            "Name" to name,
            "Address" to address,
            "Phone" to phone,
            "Email" to email,
            "Total" to sp.getPreference("cart_total"),
            "PaymentOption" to paymentOption,
            "Timestamp" to (java.sql.Timestamp(System.currentTimeMillis())).toString()
        )

        orders.add(order).addOnSuccessListener { documentReference ->
            Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")

            for (cartItem in cartList) {
                val items = cartItem.split("^").toTypedArray()

                val item = hashMapOf(
                    "Product" to items[1],
                    "Price" to items[2],
                    "Quantity" to "1"
                )
                orders.document(documentReference.id).collection("Items").add(item)
                    .addOnSuccessListener { documentReference ->
                    }.addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding item - "+items[1].toString()+"\n", e)
                    }
            }
            feedback(true)
        } .addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error adding document", e)
            feedback(false)
        }
    }

    private fun feedback(feedback: Boolean){
        val intent: Intent
        if (feedback){
            Glide.with(this).load(R.drawable.checkout_success).into(checkoutFeedbackImage)
            sp.removePreference("cart_list")
            sp.removePreference("cart_total")
            intent = Intent(this, ProductsHomeActivity::class.java)
        } else {
            Glide.with(this).load(R.drawable.checkout_failed)
                .into(checkoutFeedbackImage)
            intent = Intent(this, CheckoutActivity::class.java)
        }
        checkoutFeedback.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 7000)
    }
}