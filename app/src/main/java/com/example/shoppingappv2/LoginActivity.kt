package com.example.shoppingappv2

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.shoppingappv2.Services.SharedPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginRegister: TextView
    private lateinit var loginButton: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBar)

        auth = Firebase.auth

        loginEmail = findViewById(R.id.login_Email)

        loginPassword = findViewById(R.id.login_Password)
        loginRegister = findViewById(R.id.txt_SignUpRedirect)
        loginButton = findViewById(R.id.btn_Login)

        forgotPassword = findViewById(R.id.txt_Forgot_Password)


        // Button Register
        loginRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })

        // Button Forget Password
        forgotPassword.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.edit_userEmail)

            builder.setView(view)

            val dialog = builder.create()

            view.findViewById<Button>(R.id.btn_Reset).setOnClickListener {

                compareEmail(userEmail)
                dialog.dismiss()

            }

            view.findViewById<Button>(R.id.btn_Cancel).setOnClickListener {

                dialog.dismiss()
            }

            if (dialog.window != null) {

                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            }

            dialog.show()
        }

        // Button Login
        loginButton.setOnClickListener(View.OnClickListener {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            // Validate Email & Password
            validateEmailPassword()

            if(email.isNotEmpty() || password.isNotEmpty()){

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            val sp = SharedPreference(this)
                            sp.setPreference("isLoggedIn", "true")
                            val userEmail = auth.currentUser?.email
                            sp.setPreference("user_email", userEmail)
                            progressBar.visibility = View.VISIBLE
                            val intent = Intent(this, ProductsHomeActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        })
    }

    // Outside onCreate
    private fun compareEmail(email: EditText){

        if (email.text.toString().isEmpty()){
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }

        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                }

            }
    }

    // Validate Email & Password
    private fun validateEmailPassword() {

        val loginEmail = findViewById<EditText>(R.id.login_Email)
        val loginPassword = findViewById<EditText>(R.id.login_Password)

        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        if (email.isEmpty()) {

            loginEmail.error = "Email cannot be empty"

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.error = "Please Enter a Valid Email Address"
        } else if (password.isEmpty()) {

            loginPassword.error = "Password cannot be empty"

        }
    }

}