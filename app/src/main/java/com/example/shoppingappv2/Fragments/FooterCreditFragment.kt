package com.example.shoppingappv2.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppingappv2.R

class FooterCreditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 2023 Copyright reserved
        var view = inflater.inflate(R.layout.fragment_footer_credit, container, false);

        return view
    }
}