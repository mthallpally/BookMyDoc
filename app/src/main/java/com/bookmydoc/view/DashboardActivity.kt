package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bookmydoc.R
import com.bookmydoc.adapter.CategoryAdapter
import com.bookmydoc.adapter.DoctorAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityCategoryBinding
import com.bookmydoc.databinding.ActivityDashboardBinding
import com.bookmydoc.databinding.ActivityDocotrDetailBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Categories
import com.bookmydoc.model.User
import com.bumptech.glide.Glide
import java.util.ArrayList

class DashboardActivity : BaseActivity(), View.OnClickListener {
    private var doctorAdapyter: DoctorAdapter? = null
    private var doctorArrayList = ArrayList<String>()
    private lateinit var binding: ActivityDashboardBinding

    private var categoryAdapter: CategoryAdapter? = null
    private lateinit var categoryArrayList: ArrayList<Categories>

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
        binding.imgProfile.setOnClickListener(this)

        getUserDetails()

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
            binding.imgProfile -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun getUserDetails(){
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }
    fun userDetailSuccess(user : User){

        getCategorisList()
        hideProgressDialog()
        binding.txtUserName.text = "${user.fullName}"
        Glide.with(this)
            .load(user.image)
            .into(binding.imgProfile)
    }
    private fun getCategorisList() {
        FireStoreClass().getCategoriesList(this@DashboardActivity)
    }
    fun successCategoryListFromFirestore(productList: ArrayList<Categories>) {
        hideProgressDialog()
        categoryArrayList = productList
        categoryAdapter = CategoryAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                val intent = Intent(this@DashboardActivity, DoctorListActivity::class.java)
                intent.putExtra("category", categoryArrayList.get(position))
                startActivity(intent)
            }

        }, 3)
        val layoutManager3 = GridLayoutManager(this, 3)
        binding.rvMorningSlot.layoutManager = layoutManager3
        binding.rvMorningSlot.adapter = categoryAdapter
        categoryAdapter!!.setUpcomingList(this, categoryArrayList)


    }

}