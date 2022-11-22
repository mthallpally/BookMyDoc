package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmydoc.R
import com.bookmydoc.adapter.DoctorAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDashboardBinding
import com.bookmydoc.databinding.ActivityDoctorListBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Categories
import com.bookmydoc.model.Doctors
import java.util.ArrayList

class DoctorListActivity : BaseActivity(), View.OnClickListener {

    private var doctorAdapyter: DoctorAdapter? = null
    private var doctorArrayList = ArrayList<String>()
    private lateinit var binding: ActivityDoctorListBinding
    var categoryName: String = ""
    var categoryID: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_list)
        binding.imgBack.setOnClickListener(this)
        categoryName = intent.getStringExtra("category").toString()
        categoryID = intent.getIntExtra("categoryId", 0)
        binding.txtTitle.text = categoryName

        if (categoryID == 0)
            FireStoreClass().getAllDoctorList(this, categoryID)
        else
            FireStoreClass().getDoctorList(this, categoryID)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imgBack -> {
                onBackPressed()
            }
        }
    }

    fun successDoctorListFromFirestore(doctorArrayList: ArrayList<Doctors>) {
        Log.e("TAG", "doctorArrayList-" + doctorArrayList.size)
        doctorAdapyter = DoctorAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                val intent = Intent(this@DoctorListActivity, DocotrDetailActivity::class.java)
                intent.putExtra("drId", doctorArrayList.get(position).doctor_id)
                startActivity(intent)
            }

        }, 3)
        binding.rvMorningSlot.layoutManager = LinearLayoutManager(this)
        binding.rvMorningSlot.adapter = doctorAdapyter


        doctorAdapyter!!.setUpcomingList(this, doctorArrayList)
    }
}