package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bookmydoc.R
import com.bookmydoc.adapter.CategoryAdapter
import com.bookmydoc.adapter.SlotAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityCategoryBinding
import com.bookmydoc.databinding.ActivityDocotrDetailBinding
import com.bookmydoc.interfaces.ListSelector
import java.util.ArrayList

class CategoryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCategoryBinding
    private var categoryAdapter: CategoryAdapter? = null
    private var categoryArrayList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.imgBack.setOnClickListener(this)
        categoryAdapter = CategoryAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                val intent = Intent(this@CategoryActivity, DoctorListActivity::class.java)
                intent.putExtra("category", categoryArrayList.get(position))
                startActivity(intent)
            }

        }, 3)
        var layoutManager3 = GridLayoutManager(this, 3)
        binding!!.rvMorningSlot.layoutManager = layoutManager3
        binding!!.rvMorningSlot.adapter = categoryAdapter

        categoryArrayList.add("Audiologist")
        categoryArrayList.add("Brain")
        categoryArrayList.add("Cardiologist")
        categoryArrayList.add("Dental")
        categoryArrayList.add("Eye")
        categoryArrayList.add("Neurologist")
        categoryArrayList.add("Orthopedist")
        categoryArrayList.add("Podiatrist")
        categoryArrayList.add("Psychiatrist")
        categoryArrayList.add("Surgeon")
        categoryArrayList.add("Urologist")

        categoryAdapter!!.setUpcomingList(this, categoryArrayList)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imgBack -> {
                onBackPressed()
            }
        }
    }
}