package com.example.attendance.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.example.attendance.model.ClockHistoryResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Adapter(val context: Context, val dataList: List<ClockHistoryResponse>):
    RecyclerView.Adapter<Adapter.AdapterHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.activity_item_log, parent, false)
        return AdapterHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        val index = dataList[position]

        val date = index.createdAt?.getDateWithServerTimeStamp()

        holder.clockInText.text = index.clockIn
        holder.clockOutText.text = index.clockOut
        holder.dateText.text = SimpleDateFormat("d", Locale.getDefault()).format(date)
        holder.dayText.text = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(date)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class AdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var clockInText: TextView = itemView.findViewById(R.id.clockIn_Log)
        var clockOutText: TextView = itemView.findViewById(R.id.clockOut_Log)
        var dateText: TextView = itemView.findViewById(R.id.dateTV)
        var dayText: TextView = itemView.findViewById(R.id.dayTV)
    }

    // Converting from String to Date
    fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )
        return try {
            dateFormat.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
}