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
import com.vks.kauf.selectors.ModelSelector

class BrandAdapter(private val brandList: ArrayList<String>): RecyclerView.Adapter<BrandAdapter.ViewHolder>()
{

    companion object
    {
        lateinit var selectedBrand: String
    }
    private lateinit var parent1: ViewGroup

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val brand: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        parent1 = parent
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_brand, parent, false)
        val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.slide_in_bottom)
        itemView.startAnimation(connectingAnimation)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int
    {
        return brandList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.brand.text = brandList[position]

        holder.itemView.setOnClickListener {
            val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.fade_out_fade_in)
            holder.itemView.startAnimation(connectingAnimation)
            selectedBrand = brandList[position]
            val intent = Intent(parent1.context, ModelSelector::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            parent1.context.startActivity(intent)
        }
    }
}