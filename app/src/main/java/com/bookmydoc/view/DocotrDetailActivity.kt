package com.bookmydoc.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bookmydoc.R
import com.bookmydoc.adapter.SlotAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDocotrDetailBinding
import com.bookmydoc.horizontalcalendar.HorizontalCalendar
import com.bookmydoc.horizontalcalendar.HorizontalCalendarView
import com.bookmydoc.horizontalcalendar.utils.HorizontalCalendarListener
import com.bookmydoc.interfaces.ListSelector
import java.util.*

class DocotrDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDocotrDetailBinding
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var currendate: String = ""
    private var mSlotAdapter: SlotAdapter? = null
    var fromTime: String = ""

    private var timeSlotMorrningList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_docotr_detail)
        datePicker()

        mSlotAdapter = SlotAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                fromTime = mSlotAdapter!!.itemList!!.get(position)

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

    fun datePicker() {
        val startDate = Calendar.getInstance()

        day = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        month = Calendar.getInstance()[Calendar.MONTH]
        year = Calendar.getInstance()[Calendar.YEAR]
        currendate = (year.toString() + "-" + (month + 1) + "-" + day).toString()
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

                currendate =
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
}