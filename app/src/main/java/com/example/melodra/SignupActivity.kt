package com.example.melodra

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.melodra.databinding.ActivitySignupBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    lateinit var  binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the action bar

        binding=ActivitySignupBinding.inflate(layoutInflater)



        enableEdgeToEdge()
        setContentView(binding.root)

        binding.createAccountBtn.setOnClickListener{
            val email=binding.emailEdittext.text.toString()
            val password=binding.passwordEdittext.text.toString()
            val confirmPassword=binding.confirmPasswordEdittext.text.toString()

            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),email)){
                binding.emailEdittext.setError("Invalid email")
                return@setOnClickListener
            }
            if(password.length<6){
                binding.passwordEdittext.setError("Password must be at least 6 characters")
                return@setOnClickListener
            }
            if(!password.equals(confirmPassword)){
                binding.confirmPasswordEdittext.setError("Please ensure both passwords are identical")
                return@setOnClickListener

            }

            createAccountWithFirebase(email,password)


        }


        binding.gotoLoginBtn.setOnClickListener{
            finish()        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }
    fun createAccountWithFirebase(email:String,password:String){
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                setInProgress(false)
                Toast.makeText(applicationContext,"Create account successfully",Toast.LENGTH_SHORT).show()
                finish()
            }

            .addOnFailureListener{
                setInProgress(false)
                Toast.makeText(applicationContext,"This account already exists. Try signing in instead",Toast.LENGTH_SHORT).show()

            }
    }
    fun setInProgress(inProgress:Boolean){
        if(inProgress){
            binding.createAccountBtn.visibility= View.GONE
            binding.progressBar.visibility=View.VISIBLE
        }else{
            binding.createAccountBtn.visibility= View.VISIBLE
            binding.progressBar.visibility=View.GONE
        }
    }
}