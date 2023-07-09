package com.vks.kauf.login

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vks.kauf.MainActivity
import com.vks.kauf.R
import com.vks.kauf.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity()
{
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val fireStore = Firebase.firestore
        
        binding.btnSignup.setOnClickListener{
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordConfirm = binding.etPasswordConfirm.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty())
            {
                if (password == passwordConfirm)
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                                if (it.isSuccessful)
                                {
                                    val userid = firebaseAuth.currentUser?.uid.toString()
                                    val user = hashMapOf(
                                        "name" to name,
                                        "email" to email)

                                    fireStore.collection("users").document(userid).set(user).addOnCompleteListener {
                                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                                    }

                                    val intent = Intent(this, MainActivity::class.java )
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                                else
                                {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else
                {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvLoginRedirect.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }


    override fun finish()
    {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onBackPressed()
    {
        finish()
    }
}