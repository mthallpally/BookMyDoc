package com.bookmydoc.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bookmydoc.R
import com.bookmydoc.adapter.SlotAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDocotrDetailBinding
import com.bookmydoc.firestore.FireStoreClass
import com.bookmydoc.horizontalcalendar.HorizontalCalendar
import com.bookmydoc.horizontalcalendar.HorizontalCalendarView
import com.bookmydoc.horizontalcalendar.utils.HorizontalCalendarListener
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Doctors
import java.util.*

import com.bookmydoc.adapter.CustomDropDownAdapter
import com.bookmydoc.model.Booking
import com.bookmydoc.model.User
import com.bumptech.glide.Glide


class DocotrDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDocotrDetailBinding
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    private var mSlotAdapter: SlotAdapter? = null
    var drId: String = ""
    var selectedTopic: String = ""
    var selectedSlot: String = ""
    var selectedTime: String = ""
    var userName: String = ""

    private lateinit var mDoctorDetails: Doctors
    private var timeSlotMorrningList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_docotr_detail)
        binding.btnBook.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.imgCall.setOnClickListener(this)
        binding.imgMap.setOnClickListener(this)

        drId = intent.getStringExtra("drId").toString()
        showProgressDialog("")
        FireStoreClass().geDoctorDetails(this, drId)
        datePicker()

        mSlotAdapter = SlotAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                selectedSlot = mSlotAdapter!!.itemList!!.get(position)

            }

        }, 3)
        var layoutManager3 = GridLayoutManager(this, 4)
        binding!!.rvMorningSlot.layoutManager = layoutManager3
        binding!!.rvMorningSlot.adapter = mSlotAdapter
        timeSlotMorrningList.add("10:00 AM")
        timeSlotMorrningList.add("11:00 AM")
        timeSlotMorrningList.add("12:00 PM")
        timeSlotMorrningList.add("02:00 PM")
        timeSlotMorrningList.add("03:00 PM")
        timeSlotMorrningList.add("04:00 PM")
        mSlotAdapter!!.setUpcomingList(this, timeSlotMorrningList)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnBook -> {
                if (TextUtils.isEmpty(selectedSlot)) {
                    showErrorSnackBar("Please select slot.", true)
                } else if (TextUtils.isEmpty(selectedTopic)) {
                    showErrorSnackBar("Please choose topic.", true)
                } else {
                    addBooking()
                }

            }
            binding.imgBack -> {
                onBackPressed()
            }
            binding.imgMap -> {
                val geoUri =
                    "http://maps.google.com/maps?q=loc:" + mDoctorDetails.latitude + "," + mDoctorDetails.longitude + " (" + mDoctorDetails.name + ")"

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(geoUri)
                )
                startActivity(intent)
            }
            binding.imgCall -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + mDoctorDetails.mobile_number)
                startActivity(intent)
            }
        }
    }

    fun datePicker() {
        val startDate = Calendar.getInstance()

        day = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        month = Calendar.getInstance()[Calendar.MONTH]
        year = Calendar.getInstance()[Calendar.YEAR]
        selectedTime = (year.toString() + "-" + (month + 1) + "-" + day).toString()
        startDate[year, month] = day
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 1)
        //getSlots(currendate, (activity as CreateBookingActivity).crateBookingRequest.serviceObj.service_overview.service_id)
        val horizontalCalendar: HorizontalCalendar = HorizontalCalendar.Builder(
            binding!!.root,
            R.id.calendarView
        )
            .range(startDate, endDate)
            .mode(HorizontalCalendar.Mode.DAYS)
            .datesNumberOnScreen(4)
            .configure().formatBottomText("EEE").formatMiddleText("dd").formatTopText("MMM yyyy")
            .showBottomText(true)
            .textSize(10.00f, 10.00f, 10.00f)
            .end()
            .defaultSelectedDate(startDate)

            .build()

        horizontalCalendar.setCalendarListener(object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {

                selectedTime =
                    date[Calendar.YEAR].toString() + "-" + (date[Calendar.MONTH] + 1) + "-" + date[Calendar.DATE]


                //getAvailableTimeSlots()

            }

            override fun onCalendarScroll(
                calendarView: HorizontalCalendarView?,
                dx: Int, dy: Int
            ) {
            }

            override fun onDateLongClicked(date: Calendar?, position: Int): Boolean {
                return true
            }
        })
    }

    fun doctorDetailsSuccess(doctor: Doctors) {
        mDoctorDetails = doctor
        hideProgressDialog()
        FireStoreClass().getUserDetails(this)
        binding.txtDrName.text = doctor.name
        binding.txtCategory.text = doctor.categories
        binding.txtPatient.text = doctor.patient
        binding.txtExperience.text = doctor.experience
        binding.txtRating.text = doctor.rating.toString()


        val customDropDownAdapter = CustomDropDownAdapter(this, doctor.topic)

        binding.mSpinnerCity.adapter = customDropDownAdapter
        /*binding.mSpinnerCity.setAdapter(
            NothingSelectedSpinnerAdapter(
                adapterTransmit,
                R.layout.layout_spiner,
                this
            )
        )*/
        binding.mSpinnerCity.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedTopic = doctor.topic.get(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


    }

    fun addBooking() {
        val booking = Booking(
            System.currentTimeMillis().toString(),
            drId,
            FireStoreClass().getCurrentUserId(),
            mDoctorDetails.name,
            userName,
            selectedTopic,
            selectedSlot,
            selectedTime
        )
        FireStoreClass().addBooking(this, booking)
    }

    fun userBookingSuccess() {
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra("drName", mDoctorDetails.name)
        startActivity(intent)
    }

    fun userDetailSuccess(user: User) {

        userName = user.fullName
    }

}