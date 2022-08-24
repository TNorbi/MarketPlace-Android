package com.example.marketplaceproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.marketplaceproject.R

class AfterNewOrderDialogFragment : DialogFragment() {

    private lateinit var ongoingOrdersHyperlink : TextView
    private lateinit var exitButton: ImageButton
    private lateinit var closeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_after_new_order_dialog, container, false)

        view?.apply {
            initializeView(this)
            initializeListeners(this)
        }

        return view
    }

    private fun initializeListeners(view: View) {
        exitButton.setOnClickListener {
            dismiss()
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        ongoingOrdersHyperlink.setOnClickListener {
            Toast.makeText(context, "Ongoing orders hyperlink clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeView(view: View) {
        exitButton = view.findViewById(R.id.after_order_exit_button)
        closeButton = view.findViewById(R.id.after_order_close_button)
        ongoingOrdersHyperlink = view.findViewById(R.id.ongoing_orders_hyperlink)
    }
}