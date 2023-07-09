package com.vks.kauf.selectors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vks.kauf.R
import com.vks.kauf.helper.BrandAdapter
import com.vks.kauf.helper.CategoryAdapter
import com.vks.kauf.helper.Model
import com.vks.kauf.helper.ModelAdapter
import com.vks.kauf.databinding.ActivityModelSelectorBinding

class ModelSelector : AppCompatActivity()
{
    private lateinit var binding: ActivityModelSelectorBinding
    private lateinit var selTitle: String
    private lateinit var selDescription: String
    private lateinit var recyclerViewModel: RecyclerView
    private lateinit var modelList: ArrayList<Model>

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityModelSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvTitle.text = BrandAdapter.selectedBrand

        ///recyckler modeli
        modelList = arrayListOf()

        db.collection("items").whereEqualTo("category",CategoryAdapter.selectedCategory)
            .whereEqualTo("brand", BrandAdapter.selectedBrand)
            .orderBy("price", Query.Direction.ASCENDING)
            .get(Source.SERVER)
            .addOnSuccessListener {
                if(!it.isEmpty)
                {
                    for (data in it.documents)
                    {
                        val model: Model? = data.toObject(Model::class.java)
                        if (model != null)
                        {
                            modelList.add(model)
                        }
                    }
                    recyclerViewModel.adapter = ModelAdapter(modelList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,  it.toString(), Toast.LENGTH_SHORT).show()
            }
        recyclerViewModel = findViewById(R.id.rvModels)
        val verticalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewModel.layoutManager = verticalLayoutManager

        binding.iv1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.circle))
        val connectingAnimation = AnimationUtils.loadAnimation(this@ModelSelector, R.anim.breathing)
        binding.iv1.startAnimation(connectingAnimation)
    }
}