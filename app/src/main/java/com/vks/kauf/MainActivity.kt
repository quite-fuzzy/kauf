package com.vks.kauf


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vks.kauf.databinding.ActivityMainBinding
import com.vks.kauf.fragments.FragmentAbout
import com.vks.kauf.fragments.FragmentAppSettings
import com.vks.kauf.helper.Announcement
import com.vks.kauf.helper.AnnouncementAdapter
import com.vks.kauf.helper.CategoryAdapter
import com.vks.kauf.helper.FlashSaleAdapter
import com.vks.kauf.helper.Model
import com.vks.kauf.login.BootActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity()
{

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerViewAnn: RecyclerView
    private lateinit var recyclerViewCat: RecyclerView
    private lateinit var recyclerViewFlashSale: RecyclerView
    private lateinit var announcementList: ArrayList<Announcement>
    private lateinit var flashSaleList: ArrayList<Model>
    private lateinit var hiddenView1: ConstraintLayout
    private lateinit var cardView1: CardView
    private lateinit var hiddenView2: ConstraintLayout
    private lateinit var cardView2: CardView
    private val db = Firebase.firestore

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        ///layout inflater
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///drawer
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()


            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        binding.cv.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_out))
                        binding.cv.visibility = View.GONE
                    }
                    R.id.nav_settings -> {
                        binding.cv.visibility = View.VISIBLE
                        binding.cv.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_in))
                        supportFragmentManager.beginTransaction().replace(R.id.fc1, FragmentAppSettings()).commit()
                    }
                    R.id.nav_account -> {
                    }
                    R.id.nav_location -> {
                    }
                    R.id.nav_about -> {
                        binding.cv.visibility = View.VISIBLE
                        binding.cv.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_in))
                        supportFragmentManager.beginTransaction().replace(R.id.fc1, FragmentAbout()).commit()
                    }
                    R.id.nav_logout -> {
                        firebaseAuth.signOut()
                        val intent = Intent(this@MainActivity, BootActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                it.isChecked = true
                drawerLayout.close()
                true
            }
        }

        binding.tvWelcomeName.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val navView = findViewById<NavigationView>(R.id.navView)
        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.nav_home)
        }



        //time and date
        val df: DateFormat = SimpleDateFormat("HH")
        val date: String = df.format(Calendar.getInstance().time)

        //firebase and firestore
        firebaseAuth = FirebaseAuth.getInstance()

        val userid = firebaseAuth.currentUser?.uid.toString()
        val dbRefUser = db.collection("users").document(userid)

        ///welcome message
        lateinit var userName: String
        dbRefUser.get().addOnSuccessListener { document ->
                if (document != null)
                {
                    userName = document.get("name").toString()
                    if(date.toInt() in 5..9)
                    {
                        binding.tvWelcomeName.text = "Good morning, " + userName + "."
                        binding.tvWelcomeName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left_fade_in))
                    }
                    else if (date.toInt() in 10..17)
                    {
                        binding.tvWelcomeName.text = "Good afternoon, " + userName + "."
                        binding.tvWelcomeName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left_fade_in))
                    }
                    else if (date.toInt() >= 18 || date.toInt() <= 4)
                    {
                        binding.tvWelcomeName.text = "Good evening, " + userName + "."
                        binding.tvWelcomeName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left_fade_in))
                    }
                }
                else
                {
                    Log.d("TRPANJE", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TRPANJE", "get failed with ", exception)
            }

        ///annoucements
        recyclerViewAnn = findViewById(R.id.rvAnnouncements)
        val horizontalLayoutManagerAnn = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAnn.layoutManager = horizontalLayoutManagerAnn

        announcementList = arrayListOf()
        db.collection("announcements").orderBy("date", Query.Direction.DESCENDING).limit(3).get()
            .addOnSuccessListener {
                if(!it.isEmpty)
                {
                    for (data in it.documents)
                    {
                        val announcement: Announcement? = data.toObject(Announcement::class.java)
                        if (announcement != null)
                        {
                            announcementList.add(announcement)
                        }
                    }
                    recyclerViewAnn.adapter = AnnouncementAdapter(announcementList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,  it.toString(), Toast.LENGTH_SHORT).show()
            }

        ///categories
        val categoryList = ArrayList<String>()
        val categoryRef = db.collection("items").get().addOnSuccessListener { documents ->
            for(document in documents)
            {
                if(document != null)
                {
                    val s = document.get("category").toString()
                    if(categoryList.isEmpty() || !(categoryList.contains(s)))
                    {
                        categoryList.add(s)
                    }
                }
            }
            recyclerViewCat.adapter = CategoryAdapter(categoryList)
        }

        recyclerViewCat = findViewById(R.id.rvCategories)
        val horizontalLayoutManagerCat = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCat.layoutManager = horizontalLayoutManagerCat



        ///flash sales
        recyclerViewFlashSale = findViewById(R.id.rvFlashSale)
        val horizontalLayoutManagerFlashSale = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFlashSale.layoutManager = horizontalLayoutManagerFlashSale

        flashSaleList = arrayListOf()
        db.collection("items")
            .whereEqualTo("flash_sale", true)
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty)
                {
                    for (data in it.documents)
                    {
                        val model: Model? = data.toObject(Model::class.java)
                        if (model != null)
                        {
                            flashSaleList.add(model)
                        }
                    }
                        recyclerViewFlashSale.adapter = FlashSaleAdapter(flashSaleList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,  it.toString(), Toast.LENGTH_SHORT).show()
            }

            cardView1 = findViewById(R.id.cvfinancingCard1)
            hiddenView1 = findViewById(R.id.hidden1)
            cardView2 = findViewById(R.id.cvfinancingCard2)
            hiddenView2 = findViewById(R.id.hidden2)

            binding.cvfinancingCard1.setOnClickListener { view ->

                if (hiddenView1.getVisibility() === View.VISIBLE)
                {
                    TransitionManager.beginDelayedTransition(cardView1, AutoTransition())
                    hiddenView1.visibility = View.GONE
                } else
                {
                    TransitionManager.beginDelayedTransition(cardView1, AutoTransition())
                    hiddenView1.visibility = View.VISIBLE
                }
            }

        binding.cvfinancingCard2.setOnClickListener { view ->

            if (hiddenView2.getVisibility() === View.VISIBLE)
            {
                TransitionManager.beginDelayedTransition(cardView2, AutoTransition())
                hiddenView2.visibility = View.GONE
            } else
            {
                TransitionManager.beginDelayedTransition(cardView2, AutoTransition())
                hiddenView2.visibility = View.VISIBLE
            }
        }


        val connectingAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.breathing)
        binding.iv1.startAnimation(connectingAnimation)

        val connectingAnimation2 = AnimationUtils.loadAnimation(this@MainActivity, R.anim.breathing_left_2)
        binding.iv2.startAnimation(connectingAnimation2)


    }
}