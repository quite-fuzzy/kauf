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
import com.vks.kauf.databinding.ActivityBrandSelectorBinding
import com.vks.kauf.helper.BrandAdapter
import com.vks.kauf.helper.CategoryAdapter
import com.vks.kauf.helper.Model
import com.vks.kauf.helper.ModelAdapter

class BrandSelector : AppCompatActivity()
{
    private lateinit var binding: ActivityBrandSelectorBinding
    private lateinit var recyclerViewBrand: RecyclerView
    private lateinit var modelList: ArrayList<Model>
    private lateinit var recyclerViewModel: RecyclerView

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///recycler za listu brendova
        val brandList = ArrayList<String>()
        binding.tvTitle.text = CategoryAdapter.selectedCategory + "s"

        val brandRef = db.collection("items")
            .whereEqualTo("category", CategoryAdapter.selectedCategory)
            .get()
            .addOnSuccessListener { documents ->
            for (document in documents)
            {
                if (document != null)
                {
                    val s = document.get("brand").toString()

                    if (brandList.isEmpty() || !(brandList.contains(s)))
                    {
                        brandList.add(s)
                    }
                }
            }
            recyclerViewBrand.adapter = BrandAdapter(brandList)
        }
        recyclerViewBrand = findViewById(R.id.rvBrands)
        val verticalLayoutManagerCat = LinearLayoutManager(this@BrandSelector, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBrand.layoutManager = verticalLayoutManagerCat

        binding.iv1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.circle))
        val connectingAnimation = AnimationUtils.loadAnimation(this@BrandSelector, R.anim.breathing_left)
        binding.iv1.startAnimation(connectingAnimation)

        ///lista svih laptopova
        modelList = arrayListOf()

        db.collection("items").whereEqualTo("category",CategoryAdapter.selectedCategory)
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
        recyclerViewModel = findViewById(R.id.rvAll)
        val verticalLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewModel.layoutManager = verticalLayoutManager1

    }
}