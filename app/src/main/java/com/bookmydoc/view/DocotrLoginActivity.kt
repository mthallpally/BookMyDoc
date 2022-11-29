package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bookmydoc.Constants
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDocotrLoginBinding
import com.bookmydoc.databinding.ActivityRegisterBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.model.Doctors

class DocotrLoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDocotrLoginBinding
    private lateinit var mDoctorDetails: Doctors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_docotr_login)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnLogin -> {
                loginDoctor()
            }

        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.mEdtEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter an email id.", true)
                false
            }
            else -> {
                true
            }

        }
    }

    private fun loginDoctor() {

        if (validateLoginDetails()) {

            showProgressDialog(getString(R.string.please_wait))
            val drId: String = binding.mEdtEmail.text.toString().trim { it <= ' ' }

            FireStoreClass().geDoctorDetails(this, drId)
        }
    }

    fun doctorDetailsSuccess(doctor: Doctors) {
        mDoctorDetails = doctor
        hideProgressDialog()
        val intent = Intent(this@DocotrLoginActivity,DoctorDashboardActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS,mDoctorDetails)
        startActivity(intent)
    }
}