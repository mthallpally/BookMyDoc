package com.bookmydoc.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityRegisterBinding

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
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}