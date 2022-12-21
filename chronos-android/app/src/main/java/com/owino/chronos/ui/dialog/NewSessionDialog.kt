package com.owino.chronos.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.owino.chronos.R

class NewSessionDialog(context: Context, private val createSessionCallback: CreateNewSessionCallback) :
    Dialog(context) {
    private lateinit var sessionHoursEditText: EditText
    private lateinit var submitButton: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_new_session_layout)
        initializeResources()
    }

    private fun initializeResources() {
        sessionHoursEditText = findViewById(R.id.dialog_new_session_edit_text)
        submitButton = findViewById(R.id.dialog_new_session_submit_button)
        submitButton.setOnClickListener {
            val targetHours = sessionHoursEditText.text.toString().toInt()
            createSessionCallback.createNewSession(targetHours)
        }
    }

    interface CreateNewSessionCallback {
        fun createNewSession(targetHours: Int)
    }
}