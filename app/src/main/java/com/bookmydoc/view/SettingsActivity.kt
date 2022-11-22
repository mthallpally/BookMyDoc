package com.bookmydoc.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bookmydoc.Constants
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityCategoryBinding
import com.bookmydoc.databinding.ActivitySettingsBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        binding.imgBack.setOnClickListener(this)
        binding.linProfile.setOnClickListener(this)
        binding.linAppointment.setOnClickListener(this)
        binding.linLogout.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBack -> {
                onBackPressed()
            }
            R.id.linProfile -> {
                val intent = Intent(this@SettingsActivity,UserProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                startActivity(intent)
            }
            R.id.linAppointment -> {
                val intent = Intent(this@SettingsActivity,MyBookingActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                startActivity(intent)
            }
            R.id.linLogout -> {
                alertDialogLogout()
            }

        }
    }

    fun userDetailSuccess(user : User){
        mUserDetails = user
        hideProgressDialog()
        binding.txtEmail.text = user.email
        binding.txtUserName.text = "${user.fullName}"
        Glide.with(this)
            .load(user.image)
            .into(binding.imgProfile)
    }

    private fun getUserDetails(){
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }

    override fun onResume() {
        getUserDetails()
        super.onResume()
    }

    private fun alertDialogLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage("Are you sure you want to Logout?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}