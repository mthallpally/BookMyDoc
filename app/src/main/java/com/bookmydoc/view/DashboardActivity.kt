package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDashboardBinding
import com.bookmydoc.databinding.ActivityDocotrDetailBinding

class DashboardActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.linPopular.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.linPopular -> {
                val intent = Intent(this, DocotrDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }
}