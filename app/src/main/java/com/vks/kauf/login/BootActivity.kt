package com.vks.kauf.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.vks.kauf.MainActivity
import com.vks.kauf.login.Boot
import com.vks.kauf.R
import com.vks.kauf.databinding.ActivityBootBinding


class BootActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityBootBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityBootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java )
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Boot.darkMode = "true"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            else
            {
                Boot.darkMode = "false"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.cbMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                Boot.darkMode = "system"
                binding.tvLight.isEnabled = false
                binding.tvDark.isEnabled = false
                binding.switch1.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out))
                binding.switch1.visibility = View.INVISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            if (!isChecked)
            {
                binding.tvLight.isEnabled = true
                binding.tvDark.isEnabled = true
                binding.switch1.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
                binding.switch1.visibility = View.VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            val connectingAnimation = AnimationUtils.loadAnimation(this@BootActivity, R.anim.breathing_left_detail)
            binding.iv1.startAnimation(connectingAnimation)
        }
    }
}


