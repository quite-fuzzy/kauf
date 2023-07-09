package com.vks.kauf.helper

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vks.kauf.R
import com.vks.kauf.selectors.BrandSelector

class CategoryAdapter(private val categoryList: ArrayList<String>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>()
{
    companion object
    {
        lateinit var selectedCategory: String
    }

    private lateinit var parent1: ViewGroup

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val name: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        parent1 = parent
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_cat, parent, false)
        val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.slide_in_right_menu)
        itemView.startAnimation(connectingAnimation)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int
    {
        return categoryList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.name.text = categoryList[position] + "s"

        holder.itemView.setOnClickListener {
            val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.fade_out_fade_in)
            holder.itemView.startAnimation(connectingAnimation)
            val intent = Intent(parent1.context, BrandSelector::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            selectedCategory = categoryList[position]
            parent1.context.startActivity(intent)
        }
    }
}