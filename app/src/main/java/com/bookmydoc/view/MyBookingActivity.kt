package com.bookmydoc.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmydoc.Constants
import com.bookmydoc.R
import com.bookmydoc.adapter.DoctorAdapter
import com.bookmydoc.adapter.MybookingAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityMyBookingBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Booking
import com.bookmydoc.model.User
import java.util.ArrayList

class MyBookingActivity : BaseActivity(), View.OnClickListener {
    private var myBookingAdapyter: MybookingAdapter? = null
    private var doctorArrayList = ArrayList<String>()
    private lateinit var binding: ActivityMyBookingBinding

    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_booking)
        binding.imgBack.setOnClickListener(this)
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!}
        FireStoreClass().getMyBookingList(this, mUserDetails.id)

    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imgBack -> {
                onBackPressed()
            }
        }
    }
    fun successBookingListFromFirestore(doctorArrayList: ArrayList<Booking>) {
        hideProgressDialog()
        Log.e("TAG", "doctorArrayList-" + doctorArrayList.size)
        myBookingAdapyter = MybookingAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

            }

        }, 3)
        binding.rvMorningSlot.layoutManager = LinearLayoutManager(this)
        binding.rvMorningSlot.adapter = myBookingAdapyter


        myBookingAdapyter!!.setUpcomingList(this, doctorArrayList)
    }
}