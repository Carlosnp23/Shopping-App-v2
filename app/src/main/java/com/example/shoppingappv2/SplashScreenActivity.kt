package com.example.shoppingappv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.shoppingappv2.Services.SharedPreference
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sp = SharedPreference(this)

        FirebaseApp.initializeApp(this)
        val auth = Firebase.auth
        val userEmail = auth.currentUser?.email

        Log.e("User", userEmail.toString())

        Handler(Looper.getMainLooper()).postDelayed({
            var isLoggedIn = sp.getPreference("isLoggedIn")
            if (isLoggedIn == "true") {
                if (userEmail == null) {
                    sp.setPreference("isLoggedIn", "false")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, ProductsHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 5000)
    }
}