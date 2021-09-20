package com.example.attendance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.attendance.R
import kotlinx.android.synthetic.main.fragment_dialog.view.*

class CustomDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_dialog, container, false)

        rootView.reset_btn.setOnClickListener {
            Toast.makeText(context, "Changed Successfully", Toast.LENGTH_LONG).show()
            dismiss()
        }
        return rootView
    }
}