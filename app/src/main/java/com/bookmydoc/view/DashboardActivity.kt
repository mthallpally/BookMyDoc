package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bookmydoc.R
import com.bookmydoc.adapter.CategoryAdapter
import com.bookmydoc.adapter.DoctorAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityCategoryBinding
import com.bookmydoc.databinding.ActivityDashboardBinding
import com.bookmydoc.databinding.ActivityDocotrDetailBinding
import java.util.ArrayList

class DashboardActivity : BaseActivity(), View.OnClickListener {
    private var doctorAdapyter: DoctorAdapter? = null
    private var doctorArrayList = ArrayList<String>()
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.linPopular.setOnClickListener(this)
        binding.txtViewmore.setOnClickListener(this)
        binding.txtDrViewMore.setOnClickListener(this)
        binding.cardEye.setOnClickListener(this)
        binding.cardBrain.setOnClickListener(this)
        binding.cardDental.setOnClickListener(this)
        binding.cardTopDoctor.setOnClickListener(this)



    }

    override fun onClick(view: View?) {
        when (view) {
            binding.linPopular -> {
                val intent = Intent(this, DocotrDetailActivity::class.java)
                intent.putExtra("drName", "Dr. Patrick Cruz")
                startActivity(intent)
            }
            binding.cardTopDoctor -> {
                val intent = Intent(this, DocotrDetailActivity::class.java)
                intent.putExtra("drName", "Dr. Anastasia Alana")
                startActivity(intent)
            }
            binding.txtViewmore -> {
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
            }
            binding.txtDrViewMore -> {
                val intent = Intent(this, DoctorListActivity::class.java)
                intent.putExtra("category", "Top Doctors")
                startActivity(intent)
            }
            binding.cardBrain -> {
                val intent = Intent(this, DoctorListActivity::class.java)
                intent.putExtra("category", "Brain")
                startActivity(intent)
            }
            binding.cardDental -> {
                val intent = Intent(this, DoctorListActivity::class.java)
                intent.putExtra("category", "Dental")
                startActivity(intent)
            }
            binding.cardEye -> {
                val intent = Intent(this, DoctorListActivity::class.java)
                intent.putExtra("category", "Eye")
                startActivity(intent)
            }
        }
    }
}