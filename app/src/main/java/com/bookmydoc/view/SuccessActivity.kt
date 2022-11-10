package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivitySuccessBinding

class SuccessActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySuccessBinding

    var drName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_success)
        binding.btnHome.setOnClickListener(this)
        drName = intent.getStringExtra("drName").toString()
        binding.txtDrName.text = drName+" Success"

    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnHome -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}