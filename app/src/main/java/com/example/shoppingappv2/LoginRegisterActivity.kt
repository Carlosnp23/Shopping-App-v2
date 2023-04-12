package com.example.shoppingappv2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

lateinit var loginRegisterFrame: FrameLayout
lateinit var inflater: LayoutInflater
lateinit var view: View

class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        loginRegisterFrame = findViewById(R.id.login_register_frame)
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        initRegister()
    }

    private fun initLogin(){
        currentPage = "Login"

        loginRegisterFrame.removeAllViews()

        view = inflater.inflate(R.layout.activity_login, null)
        loginRegisterFrame!!.addView(view, loginRegisterFrame!!.childCount - 1)

        loginRegisterFrame.animate()
            .alpha(1.0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    loginRegisterFrame.visibility = View.VISIBLE
                }
            })

        var login_register = findViewById<TextView>(R.id.login_register_frame)

        login_register.setOnClickListener(View.OnClickListener {
            initRegister()
        })
    }

    private fun initRegister(){
        currentPage = "Register"

        loginRegisterFrame.removeAllViews()

        view = inflater.inflate(R.layout.activity_register, null)
        loginRegisterFrame!!.addView(view, loginRegisterFrame!!.childCount - 1)

        loginRegisterFrame.animate()
            .alpha(1.0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    loginRegisterFrame.visibility = View.VISIBLE
                }
            })

        var registerLogin = findViewById<TextView>(R.id.register_Login)

        registerLogin.setOnClickListener(View.OnClickListener {
            initLogin()
        })
    }

}