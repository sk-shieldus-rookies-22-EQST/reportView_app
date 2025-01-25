package com.example.reportview_003.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reportview_003.R

class PurchaseProcessFragment: Fragment() {

    private lateinit var purchaseProcessList: ListView
    private lateinit var purchaseProcessPoint: TextView
    private lateinit var purchaseProcessTotalPrice: TextView
    private lateinit var purchaseProcessStorePoint: TextView
    private lateinit var purchaseProcessButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.purchase_process, container, false)

        return view
    }
}