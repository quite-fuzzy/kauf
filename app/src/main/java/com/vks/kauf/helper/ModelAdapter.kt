package com.vks.kauf.helper

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vks.kauf.R
import com.vks.kauf.selectors.BrandSelector
import com.vks.kauf.selectors.Detail
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class ModelAdapter(private val modelList: ArrayList<Model>): RecyclerView.Adapter<ModelAdapter.ViewHolder>()
{
    private lateinit var parent1: ViewGroup


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val model: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val stock: TextView = itemView.findViewById(R.id.tvStock)
        val flashSale: TextView = itemView.findViewById(R.id.tvPriceFlashSale)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        parent1 = parent
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_model, parent, false)
        val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.slide_in_bottom)
        itemView.startAnimation(connectingAnimation)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int
    {
        return modelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.model.text = modelList[position].model
        holder.description.text = modelList[position].description
        holder.price.text = modelList[position].price?.roundToInt().toString() + "e"

        if(modelList[position].flash_sale == true)
        {
            holder.flashSale.text = "FLASH SALE"
            holder.flashSale.visibility = View.VISIBLE
        }


        if(modelList[position].stock!! > 0)
        {
            holder.stock.text = "In stock"
        }
        else
        {
            holder.stock.text = "Out of stock"
        }

        holder.itemView.setOnClickListener {
            val connectingAnimation = AnimationUtils.loadAnimation(parent1.context, R.anim.fade_out_fade_in)
            holder.itemView.startAnimation(connectingAnimation)

            val intent = Intent(parent1.context, Detail::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("Category", modelList[position].category)
            intent.putExtra("Brand", modelList[position].brand)
            intent.putExtra("Model", modelList[position].model)
            intent.putExtra("Description", modelList[position].description)
            intent.putExtra("Price", modelList[position].price)
            intent.putExtra("Stock", modelList[position].stock)
            intent.putExtra("Flash sale", modelList[position].flash_sale)
            parent1.context.startActivity(intent)

        }
    }
}