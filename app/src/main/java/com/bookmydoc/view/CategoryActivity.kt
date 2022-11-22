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
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Categories
import java.util.ArrayList

class CategoryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCategoryBinding
    private var categoryAdapter: CategoryAdapter? = null

    private lateinit var categoryArrayList: ArrayList<Categories>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.imgBack.setOnClickListener(this)

        getCategorisList()
    }

    private fun getCategorisList() {
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getCategoriesList(this@CategoryActivity)
    }
    fun successCategoryListFromFirestore(productList: ArrayList<Categories>) {
        hideProgressDialog()
        categoryArrayList = productList
        categoryAdapter = CategoryAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                val intent = Intent(this@CategoryActivity, DoctorListActivity::class.java)
                intent.putExtra("category", categoryArrayList.get(position).name)
                intent.putExtra("categoryId", categoryArrayList.get(position).id)
                startActivity(intent)
            }

        }, categoryArrayList.size)
        val layoutManager3 = GridLayoutManager(this, 3)
        binding.rvMorningSlot.layoutManager = layoutManager3
        binding.rvMorningSlot.adapter = categoryAdapter
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