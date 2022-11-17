package com.bookmydoc.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import com.bookmydoc.Constants
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityLoginBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.model.User
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.btnLogin.setOnClickListener(this)
        binding.txtSignup.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.txtSignup -> {
                val intent = Intent(this, RegisterActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@LoginActivity,
                    (binding.ivProfile as View?)!!, "profile"
                )
                startActivity(intent, options.toBundle())

            }
            binding.btnLogin -> {
                loginUser()
            }
        }
    }

    private fun loginUser() {

        if (validateLoginDetails()) {

            showProgressDialog(getString(R.string.please_wait))

            val email: String = binding.mEdtEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.mEdtPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FireStoreClass().getUserDetails(this@LoginActivity)

                    } else {
                        hideProgressDialog()
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    hideProgressDialog()
                    Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun validateLoginDetails() : Boolean{
        return when{
            TextUtils.isEmpty(binding.mEdtEmail.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar("Please enter an email id.",true)
                false
            }
            TextUtils.isEmpty(binding.mEdtPassword.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar("Please enter a password.",true)
                false
            }
            else->{
                true
            }

        }
    }
    fun userLoggedInSuccess(user : User){
        hideProgressDialog()

        if(user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else{
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        finish()
    }
    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()

    }
}