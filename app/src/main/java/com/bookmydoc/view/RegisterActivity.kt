package com.bookmydoc.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bookmydoc.Constants
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityRegisterBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.txtLogin.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.txtLogin -> {
                onBackPressed()
            }
            binding.btnSignup -> {
                registerUser()

            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            showProgressDialog(getString(R.string.please_wait))

            val email: String = binding.mEdtEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.mEdtPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val userId = firebaseUser.uid

                        val user = User(userId,
                            binding.mEdtFullName.text.toString().trim { it <= ' ' },
                            binding.mEdtEmail.text.toString().trim { it <= ' ' },
                            binding.mEdtMobile.text.toString().toLong(),
                            binding.ccp1.selectedCountryName.toString().trim { it <= ' ' })

                        //Register the user to the FireStore firebase
                        FireStoreClass().registerUser(this, user)
                        //getting details and logged in the user
                        FireStoreClass().getUserDetails(this@RegisterActivity)
                    } else {
                        hideProgressDialog()
                        Toast.makeText(
                            this@RegisterActivity,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    hideProgressDialog()
                    Toast.makeText(this@RegisterActivity, exception.message, Toast.LENGTH_SHORT)
                        .show()
                    exception.printStackTrace()
                }
        }
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.mEdtFullName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter first name.", true)
                false
            }
            TextUtils.isEmpty(binding.mEdtEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter an email id.", true)
                false
            }
            TextUtils.isEmpty(binding.mEdtPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter a password.", true)
                false
            }
            TextUtils.isEmpty(binding.mEdtConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter confirm password.", true)
                false
            }
            binding.mEdtPassword.text.toString()
                .trim { it <= ' ' } != binding.mEdtConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar("Password and confirm password does not match", true)
                false
            }
            !binding.cbTermsAndCondition.isChecked -> {
                showErrorSnackBar("Please agree terms and conditions.", true)
                false
            }
            else -> {
                true
            }

        }
    }

    fun userRegistrationSuccess() {
        hideProgressDialog()
        Toast.makeText(this@RegisterActivity, "You are registered successfully", Toast.LENGTH_SHORT)
            .show()
    }

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()
        val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
        startActivity(intent)
        finishAffinity()
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()

    }
}