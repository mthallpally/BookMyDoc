package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.bookmydoc.model.Doctors
import com.bookmydoc.model.User
import com.bumptech.glide.Glide
import java.util.ArrayList

class DashboardActivity : BaseActivity(), View.OnClickListener {
    private var doctorAdapyter: DoctorAdapter? = null
    private var doctorArrayList = ArrayList<Doctors>()
    private lateinit var binding: ActivityDashboardBinding

    private var categoryAdapter: CategoryAdapter? = null
    private lateinit var categoryArrayList: ArrayList<Categories>

    var name = arrayOf(
        "Dr. Bill Dorfman",
        "Dr. Don Solooki",
        "Dr. Jay R. Lieberman",
        "Dr. Mark Spoonamore"
    )
    var categories = arrayOf(
        "Aesthetic Dentistry",
        "DDS, PhD, Dentist",
        "Orthopedic surgeon",
        "Orthopedic surgeon"
    )
    var categories_id = arrayOf(4, 4, 6, 6)
    var patient = arrayOf("18K", "2k", "5K", "7K")
    var experience = arrayOf("10", "5", "7", "8")
    var rating = arrayOf(4.5, 4.5, 4.0, 4.9)
    var mobile_number = arrayOf("+13102775678", "2136243333", "+18008722273", "+18008722273")
    var latitude = arrayOf(
        "40.730817928533725",
        "34.050146118647326",
        "34.06288944377939",
        "34.06284562799231"
    )
    var longitude = arrayOf(
        "-74.06492554226466",
        "-118.25371249231205",
        "-118.20267052573662",
        "-118.20282822659448"
    )

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


        for (item in name.indices) {

            val doctors = Doctors(
                System.currentTimeMillis().toString(),
                name[item],
                categories[item],
                categories_id[item],
                patient[item],
                experience[item],
                rating[item],
                mobile_number[item],
                latitude[item],
                longitude[item]
            )
            //FireStoreClass().addDoctor(this, doctors)
        }
        getUserDetails()
        FireStoreClass().getTopDoctorList(this, 4.0)
    }

    fun successDoctorListFromFirestore(doctorArrayList: ArrayList<Doctors>) {
        this.doctorArrayList = doctorArrayList
        doctorAdapyter = DoctorAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                val intent = Intent(this@DashboardActivity, DocotrDetailActivity::class.java)
                intent.putExtra("drId", doctorArrayList.get(position).doctor_id)
                startActivity(intent)
            }

        }, 3)
        binding.rvDoctors.layoutManager = LinearLayoutManager(this)
        binding.rvDoctors.adapter = doctorAdapyter
        doctorAdapyter!!.setUpcomingList(this, doctorArrayList)

        if(doctorArrayList.size>0){
            binding.txtDrName.text = doctorArrayList.get(0).name
            binding.txtCategory.text = doctorArrayList.get(0).categories
        }
    }
    fun userRegistrationSuccess() {}
    override fun onClick(view: View?) {
        when (view) {
            binding.linPopular -> {
                if(doctorArrayList.size>0){
                    val intent = Intent(this@DashboardActivity, DocotrDetailActivity::class.java)
                    intent.putExtra("drId", doctorArrayList.get(0).doctor_id)
                    startActivity(intent)
                }

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
                intent.putExtra("categoryId", 0)
                startActivity(intent)
            }

            binding.imgProfile -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getUserDetails() {
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }

    fun userDetailSuccess(user: User) {

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
                intent.putExtra("category", categoryArrayList.get(position).name)
                intent.putExtra("categoryId", categoryArrayList.get(position).id)
                startActivity(intent)
            }

        }, 3)
        val layoutManager3 = GridLayoutManager(this, 3)
        binding.rvMorningSlot.layoutManager = layoutManager3
        binding.rvMorningSlot.adapter = categoryAdapter
        categoryAdapter!!.setUpcomingList(this, categoryArrayList)


    }

}