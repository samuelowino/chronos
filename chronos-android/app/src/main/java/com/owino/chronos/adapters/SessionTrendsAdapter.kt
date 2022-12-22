package com.owino.chronos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.owino.chronos.R
import com.owino.chronos.database.entities.ChronosSession

class SessionTrendsAdapter(private val sessions: List<ChronosSession>, private val deleteClickListener: DeleteButtonClickedListener) :
    RecyclerView.Adapter<SessionTrendsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trends_layout, parent, false)
        return ViewHolder(view, deleteClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]
        holder.targetHoursTextView.text = session.targetHours.toString()
        holder.completedHoursTextView.text = session.completedHours.toString()
        holder.sessionStartDateTextView.text = session.sessionStart
        holder.sessionEndDateTextView.text = session.sessionEnd
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    class ViewHolder(itemView: View, private val deleteClickListener: DeleteButtonClickedListener) : RecyclerView.ViewHolder(itemView), OnClickListener{
        var targetHoursTextView: TextView
        var completedHoursTextView: TextView
        var sessionStartDateTextView: TextView
        var sessionEndDateTextView: TextView

        private var deleteButton: MaterialButton

        init {
            targetHoursTextView = itemView.findViewById(R.id.item_trends_target_hours_view)
            completedHoursTextView = itemView.findViewById(R.id.item_trends_coding_hours_view)
            sessionStartDateTextView = itemView.findViewById(R.id.item_trends_start_date_view)
            sessionEndDateTextView = itemView.findViewById(R.id.item_trends_end_date_view)
            deleteButton = itemView.findViewById(R.id.item_trends_delete_action_view)

            deleteButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            deleteClickListener.onDeleteClicked(adapterPosition)
        }
    }

    interface DeleteButtonClickedListener {
        fun onDeleteClicked(adapterPosition: Int)
    }
}