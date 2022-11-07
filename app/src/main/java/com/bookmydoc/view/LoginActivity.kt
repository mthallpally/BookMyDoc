package com.bookmydoc.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityLoginBinding


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
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}