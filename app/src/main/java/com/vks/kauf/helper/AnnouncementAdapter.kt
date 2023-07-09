package com.vks.kauf.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.vks.kauf.R
import com.vks.kauf.helper.AnnouncementAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class AnnouncementAdapter(private val announcementList: ArrayList<Announcement>): RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>()
{
    private lateinit var parent1: ViewGroup

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val text: TextView = itemView.findViewById(R.id.tvText)
        val date: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        parent1 = parent
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_ann, parent, false)
        val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.slide_in_right_menu)
        itemView.startAnimation(connectingAnimation)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int
    {
        return announcementList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.title.text = announcementList[position].title
        holder.text.text = announcementList[position].text

        val df: DateFormat = SimpleDateFormat("EEE, MMM d")
        val date1: String = df.format(announcementList[position].date?.time)

        holder.date.text = date1
    }
}