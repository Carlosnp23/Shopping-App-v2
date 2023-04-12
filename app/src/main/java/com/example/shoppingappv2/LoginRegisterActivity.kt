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

lateinit var login_register_frame: FrameLayout
lateinit var inflater: LayoutInflater
lateinit var view: View

class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        login_register_frame = findViewById<FrameLayout>(R.id.login_register_frame)
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        initRegister()
    }

    fun initLogin(){
        currentPage = "Login"

        login_register_frame.removeAllViews()

        view = inflater.inflate(R.layout.activity_login, null)
        login_register_frame!!.addView(view, login_register_frame!!.childCount - 1)

        login_register_frame.animate()
            .alpha(1.0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    login_register_frame.setVisibility(View.VISIBLE)
                }
            })

        var login_register = findViewById<TextView>(R.id.login_register_frame)

        login_register.setOnClickListener(View.OnClickListener {
            initRegister()
        })
    }

    private fun initRegister(){
        currentPage = "Register"

        login_register_frame.removeAllViews()

        view = inflater.inflate(R.layout.activity_register, null)
        login_register_frame!!.addView(view, login_register_frame!!.childCount - 1)

        login_register_frame.animate()
            .alpha(1.0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    login_register_frame.setVisibility(View.VISIBLE)
                }
            })

        var register_login = findViewById<TextView>(R.id.register_login)

        register_login.setOnClickListener(View.OnClickListener {
            initLogin()
        })
    }

}