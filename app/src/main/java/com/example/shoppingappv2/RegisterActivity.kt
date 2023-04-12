package com.example.shoppingappv2

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.shoppingappv2.Services.SharedPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPage = "Register"
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val registerUserName = findViewById<EditText>(R.id.register_UserName)
        val registerEmail = findViewById<EditText>(R.id.register_Email)
        val registerPassword = findViewById<EditText>(R.id.register_Password)
        val registerConfirmPassword = findViewById<EditText>(R.id.register_ConfirmPassword)
        var registerLogin = findViewById<TextView>(R.id.register_Login)
        var registerButton = findViewById<TextView>(R.id.register_Button)


        // Go to login
        registerLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        // Register User
        registerButton.setOnClickListener(View.OnClickListener {
            val userName = registerUserName.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val confirmPassword = registerConfirmPassword.text.toString()

            //var popAlert = Pop_Alert(this, this)

            if (userName.isNotEmpty() || email.isNotEmpty() ||
                password.isNotEmpty() || confirmPassword.isNotEmpty()
            ) {

                if (password == confirmPassword) {

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->

                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                                val sp = SharedPreference(this)
                                sp.setPreference("isLoggedIn", "true")
                                val userEmail = auth.currentUser?.email
                                sp.setPreference("user_email", userEmail)
                                val intent = Intent(this, ProductsHomeActivity::class.java)
                                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(
                                    ContentValues.TAG,
                                    "createUserWithEmail:failure",
                                    task.exception
                                )
                                Toast.makeText(
                                    this,
                                    "The email address is already in use by another account",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password does not matched", Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}