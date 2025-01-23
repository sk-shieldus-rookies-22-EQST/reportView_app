package com.example.reportview_003.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reportview_003.R

class BookDetailFragment: Fragment() {

    private lateinit var bookDetailImage : ImageView
    private lateinit var bookDetailTitle : TextView
    private lateinit var bookDetailPrice : TextView
    private lateinit var bookDetailCart : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.book_detail_main, container, false)
        return view
    }
}