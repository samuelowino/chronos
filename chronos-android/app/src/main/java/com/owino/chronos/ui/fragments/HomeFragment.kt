package com.owino.chronos.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.owino.chronos.ApplicationContext
import com.owino.chronos.R
import com.owino.chronos.settings.ChronosPreferences
import com.owino.chronos.ui.dialog.NewSessionDialog
import com.owino.chronos.viewModel.ChronosHomeViewModel
import com.owino.chronos.viewModel.ChronosHomeViewModel.InitSessionFailedCallback
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var viewModel: ChronosHomeViewModel
    private lateinit var hoursCountView: TextView
    private lateinit var targetHoursView: TextView
    private lateinit var minusActionView: View
    private lateinit var plusActionView: View
    private lateinit var resetButtonView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationContext =
            requireContext().applicationContext.applicationContext as ApplicationContext
        viewModel =
            ChronosHomeViewModel(applicationContext.getAppDatabase(requireContext()).sessionDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeResources(view)
        viewModel.initSession(requireContext(), object : InitSessionFailedCallback {
            override fun onSessionSuccess() {
                hoursCountView.text = viewModel.chronosSession?.completedHours.toString()
                targetHoursView.text = String.format(Locale.getDefault(), "%d %s",
                    viewModel.chronosSession?.targetHours,
                    context?.resources?.getString(R.string.general_hours))
            }

            override fun onSessionInitFailed() {
                showAlertDialog()
            }
        })
    }

    private fun initializeResources(view: View) {
        hoursCountView = view.findViewById(R.id.fragment_home_hours_text_view)
        minusActionView = view.findViewById(R.id.fragment_home_minus_action_view)
        plusActionView = view.findViewById(R.id.fragment_home_plus_action_view)
        resetButtonView = view.findViewById(R.id.fragment_home_reset_session_button)
        targetHoursView = view.findViewById(R.id.fragment_home_target_hours);

        plusActionView.setOnClickListener {
            viewModel.increaseSessionHours(requireContext())
        }

        minusActionView.setOnClickListener {
            viewModel.reduceSessionHours(requireContext())
        }

        resetButtonView.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle(requireContext().getString(R.string.general_are_you_sure))
                .setMessage(requireContext().getString(R.string.invalidate_msg))
                .setNegativeButton(requireContext().getString(R.string.general_give_up), DialogInterface.OnClickListener { dialog, which ->
                    viewModel.chronosSession = null
                    hoursCountView.text = 0.toString()
                    targetHoursView.text = 0.toString()
                    dialog.dismiss()
                    ChronosPreferences.setActiveSession(requireContext(), null)
                })
                .setPositiveButton(requireContext().getString(R.string.dont_reset_msg), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .create()
                .show()

        }
    }

    private fun showAlertDialog() {
        NewSessionDialog(requireContext(), object : NewSessionDialog.CreateNewSessionCallback {
            override fun createNewSession(targetHours: Int) {
                viewModel.createNewSession(requireContext(), targetHours)
                ChronosPreferences.setActiveSession(
                    requireContext(),
                    viewModel.currentSessionUUID!!
                )

                hoursCountView.text = viewModel.chronosSession?.completedHours.toString()
                targetHoursView.text = String.format(Locale.getDefault(), "%d %s",
                    viewModel.chronosSession?.targetHours,
                    context?.resources?.getString(R.string.general_hours))
            }
        }).show()
    }
}