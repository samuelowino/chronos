package com.owino.chronos.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.owino.chronos.ApplicationContext
import com.owino.chronos.R
import com.owino.chronos.database.entities.ChronosSession
import com.owino.chronos.events.LaunchNewSessionEvent
import com.owino.chronos.events.SessionReloadEvent
import com.owino.chronos.settings.ChronosPreferences
import com.owino.chronos.ui.dialog.NewSessionDialog
import com.owino.chronos.viewModel.ChronosHomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var viewModel: ChronosHomeViewModel
    private lateinit var hoursCountView: TextView
    private lateinit var targetHoursView: TextView
    private lateinit var minusActionView: View
    private lateinit var plusActionView: View
    private lateinit var resetButtonView: View
    private lateinit var startSessionView: View
    private lateinit var progressBar: ProgressBar
    private var newSessionDialog: NewSessionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationContext =
            requireContext().applicationContext.applicationContext as ApplicationContext
        viewModel =
            ChronosHomeViewModel(applicationContext.getAppDatabase(requireContext()).sessionDao())

        val completedHoursObserver = androidx.lifecycle.Observer<ChronosSession> { newSession ->
            hoursCountView.text = newSession.completedHours.toString()
        }

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
    }

    override fun onStart() {
        super.onStart()
        try {
            viewModel.initSession(requireContext())
            EventBus.getDefault().register(this)
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
        }
    }

    private fun initializeResources(view: View) {
        hoursCountView = view.findViewById(R.id.fragment_home_hours_text_view)
        minusActionView = view.findViewById(R.id.fragment_home_minus_action_view)
        plusActionView = view.findViewById(R.id.fragment_home_plus_action_view)
        resetButtonView = view.findViewById(R.id.fragment_home_reset_session_button)
        targetHoursView = view.findViewById(R.id.fragment_home_target_hours)
        startSessionView = view.findViewById(R.id.fragment_home_start_session_button)
        progressBar = view.findViewById(R.id.fragment_home_progress_bar)

        plusActionView.setOnClickListener {
            viewModel.increaseSessionHours(requireContext())
        }

        minusActionView.setOnClickListener {
            viewModel.reduceSessionHours(requireContext())
        }

        startSessionView.setOnClickListener {
            viewModel.invalidateCurrentSession(requireContext())
            viewModel.initSession(requireContext())
        }

        resetButtonView.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle(requireContext().getString(R.string.general_are_you_sure))
                .setMessage(requireContext().getString(R.string.invalidate_msg))
                .setNegativeButton(
                    requireContext().getString(R.string.general_give_up)
                ) { dialog, _ ->
                    viewModel.chronosSession = null
                    hoursCountView.text = 0.toString()
                    targetHoursView.text = 0.toString()
                    progressBar.progress = 0
                    dialog.dismiss()
                    ChronosPreferences.setActiveSession(requireContext(), null)
                }
                .setPositiveButton(
                    requireContext().getString(R.string.dont_reset_msg)
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()

        }
    }

    private fun showNewSessionAlertDialog() {
        newSessionDialog = NewSessionDialog(requireContext(), object : NewSessionDialog.CreateNewSessionCallback {
            override fun createNewSession(targetHours: Int) {
                viewModel.createNewSession(requireContext(), targetHours)
            }
        })
        newSessionDialog!!.show()
    }

    @Subscribe
    public fun onLaunchNewSessionEvent(event: LaunchNewSessionEvent) {
        showNewSessionAlertDialog()
    }

    @Subscribe
    public fun onSessionReloadEvent(event: SessionReloadEvent) {
        Log.e(TAG, "onSessionReloadEvent: session: " + viewModel.chronosSession!! )
        if (newSessionDialog != null){
            newSessionDialog!!.dismiss()
        }

        hoursCountView.text = viewModel.chronosSession?.completedHours.toString()
        targetHoursView.text = String.format(
            Locale.getDefault(), "%d %s",
            viewModel.chronosSession?.targetHours,
            context?.resources?.getString(R.string.general_hours)
        )

        viewModel.chronosSession?.targetHours.let {
            progressBar.max = it!!
        }

        viewModel.chronosSession?.completedHours.let {
            progressBar.progress = it!!
        }
    }
}