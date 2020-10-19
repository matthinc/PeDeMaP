package com.example.peopledensitymeasurementprototype.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.model.entity.*
import com.example.peopledensitymeasurementprototype.util.formatForLog
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class LogViewAdapter(var entries: List<LogEntity>?, private val ctx: Context) : RecyclerView.Adapter<LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.log_view_item_view, parent, false)
        return LogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return (entries ?: emptyList()).size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        entries?.let {
            val entry = it[position]

            // Log level mapping
            holder.level.text = when (entry.level) {
                LOG_LEVEL_DEBUG -> ctx.getString(R.string.log_level_debug)
                LOG_LEVEL_INFO -> ctx.getString(R.string.log_level_info)
                LOG_LEVEL_WARN -> ctx.getString(R.string.log_level_warn)
                LOG_LEVEL_ERROR -> ctx.getString(R.string.log_level_error)
                else -> ctx.resources.getString(R.string.log_level_unknown)
            }

            holder.category.text = entry.category

            holder.timestamp.text = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(entry.timestamp),
                ZoneId.systemDefault()
            ).formatForLog()

            holder.message.text = entry.message
        }
    }
}

class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val level = view.findViewById<TextView>(R.id.log_view_log_level)
    val timestamp = view.findViewById<TextView>(R.id.log_view_timestamp)
    val category = view.findViewById<TextView>(R.id.log_view_category)
    val message = view.findViewById<TextView>(R.id.log_view_message)
}
