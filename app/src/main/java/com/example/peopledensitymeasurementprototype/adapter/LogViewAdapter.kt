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
import java.util.*

class LogViewAdapter(var entries: MutableList<LogEntity>, private val ctx: Context) :
    RecyclerView.Adapter<LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.log_view_item_view, parent, false)
        return LogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val entry = entries[position]

        // Log level mapping
        val label = when (entry.level) {
            LOG_LEVEL_DEBUG -> LogLevelLabel(ctx.getString(R.string.log_level_debug), 0xff333333)
            LOG_LEVEL_INFO -> LogLevelLabel(ctx.getString(R.string.log_level_info), 0xff42bcf5)
            LOG_LEVEL_WARN -> LogLevelLabel(ctx.getString(R.string.log_level_warn), 0xffffae00)
            LOG_LEVEL_ERROR -> LogLevelLabel(ctx.getString(R.string.log_level_error), 0xfff55742)
            else -> LogLevelLabel(ctx.getString(R.string.unknown), 0xff000000)
        }

        holder.level.text = label.text
        holder.level.setTextColor(label.color.toInt())
        holder.category.text = entry.category
        holder.timestamp.text = Date(entry.timestamp * 1_000).formatForLog()
        holder.message.text = entry.message.replace(";", "\n")
    }

    private data class LogLevelLabel(val text: String, val color: Long)
}

class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val level = view.findViewById<TextView>(R.id.log_view_log_level)
    val timestamp = view.findViewById<TextView>(R.id.log_view_timestamp)
    val category = view.findViewById<TextView>(R.id.log_view_category)
    val message = view.findViewById<TextView>(R.id.log_view_message)
}
