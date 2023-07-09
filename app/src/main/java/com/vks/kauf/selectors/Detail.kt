package com.vks.kauf.selectors

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vks.kauf.R
import com.vks.kauf.databinding.ActivityDetailBinding
import kotlin.math.roundToInt


class Detail : AppCompatActivity()
{
    private lateinit var binding: ActivityDetailBinding
    private lateinit var flashSaleView: TextView
    private lateinit var priceView: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if(bundle != null)
        {
            binding.tvModel.text = bundle.getString("Model")
            binding.tvBrand.text = bundle.getString("Brand")
            binding.tvDescription.text = bundle.getString("Description")
            binding.tvPrice.text = bundle.getFloat("Price").roundToInt().toString() + "e"

            ///za lager
            if(bundle.getFloat("Stock") > 0)
            {
                binding.tvStock.text = "In stock."
            }
            else
            {
                binding.tvStock.text = "Out of stock."
            }

            ///za flash sale
            if(bundle.getBoolean("Flash sale"))
            {
                binding.tvFlashSale.text = "FLASH SALE"
                binding.tvPrice.isEnabled = false

                priceView = findViewById(R.id.tvPrice)
                flashSaleView = findViewById(R.id.tvPriceFlashSale)

                priceView.paintFlags = priceView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                val priceFlashSale = bundle.getFloat("Price") - bundle.getFloat("Price") / 10
                binding.tvPriceFlashSale.text = priceFlashSale.roundToInt().toString() + "e"

                flashSaleView.visibility = View.VISIBLE
            }

            ///za rate
            val kartica = bundle.getFloat("Price") / 24
            val finansiranje = bundle.getFloat("Price") / 36

            binding.tvCardDetail.text = "24 rate po " + kartica.roundToInt() + " eura."
            binding.tvFinancingDetail.text = "36 rata po " + finansiranje.roundToInt() + " eura."
        }

        val connectingAnimation = AnimationUtils.loadAnimation(this@Detail, R.anim.breathing_left_detail)
        binding.iv1.startAnimation(connectingAnimation)

    }
}