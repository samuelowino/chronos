package com.owino.chronos.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.owino.chronos.R
import com.owino.chronos.adapters.SessionTrendsAdapter
import com.owino.chronos.events.SessionsListLoadedEvent
import com.owino.chronos.viewModel.TrendsViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class TrendsFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SessionTrendsAdapter
    private lateinit var viewModel: TrendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = TrendsViewModel(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_trends_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeResources(view)
    }

    override fun onStart() {
        super.onStart()
        try {
            viewModel.loadOrReloadSessions()
            EventBus.getDefault().register(this)
        } catch (exception: java.lang.Exception){
            exception.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    public fun onSessionsListLoadedEvent(event: SessionsListLoadedEvent){
        adapter.notifyDataSetChanged()
    }

    private fun initializeResources(view: View){
        recyclerView = view.findViewById(R.id.fragment_trends_recycler_view)
        adapter = SessionTrendsAdapter(viewModel.sessions, object: SessionTrendsAdapter.DeleteButtonClickedListener {
            override fun onDeleteClicked(adapterPosition: Int) {
                val session = viewModel.sessions[adapterPosition]
                viewModel.deleteSession(session)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}