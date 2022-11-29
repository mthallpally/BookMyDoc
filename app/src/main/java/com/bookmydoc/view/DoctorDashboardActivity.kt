package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmydoc.Constants
import com.bookmydoc.R
import com.bookmydoc.adapter.DoctorAppoinetmentAdapter
import com.bookmydoc.adapter.MybookingAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDoctorDashboardBinding
import com.bookmydoc.databinding.ActivityMyBookingBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Booking
import com.bookmydoc.model.Doctors
import com.bookmydoc.model.User
import java.util.ArrayList

class DoctorDashboardActivity : BaseActivity(), View.OnClickListener {

    private var myBookingAdapyter: DoctorAppoinetmentAdapter? = null
    private var doctorArrayList = ArrayList<String>()
    private lateinit var binding: ActivityDoctorDashboardBinding

    private lateinit var mUserDetails: Doctors
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_dashboard)
        binding.linLogout.setOnClickListener(this)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        binding.txtUserName.text = "${mUserDetails.name}"

        FireStoreClass().getDoctorBookingList(this, mUserDetails.doctor_id)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.linLogout -> {
                startActivity(Intent(this, DocotrLoginActivity::class.java))
                finishAffinity()
            }
        }
    }
    fun successBookingListFromFirestore(doctorArrayList: ArrayList<Booking>) {
        hideProgressDialog()
        Log.e("TAG", "doctorArrayList-" + doctorArrayList.size)
        myBookingAdapyter = DoctorAppoinetmentAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

            }

        }, 3)
        binding.rvDoctors.layoutManager = LinearLayoutManager(this)
        binding.rvDoctors.adapter = myBookingAdapyter
        myBookingAdapyter!!.setUpcomingList(this, doctorArrayList)
    }
}