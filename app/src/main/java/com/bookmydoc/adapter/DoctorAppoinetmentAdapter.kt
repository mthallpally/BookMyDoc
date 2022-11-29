package com.bookmydoc.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bookmydoc.R
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ViewBookingTimeBinding
import com.bookmydoc.databinding.ViewCategoryBinding
import com.bookmydoc.databinding.ViewDoctorBinding
import com.bookmydoc.databinding.ViewMyBookingBinding
import com.bookmydoc.interfaces.ListSelector
import com.bookmydoc.model.Booking
import com.bookmydoc.model.Doctors

class DoctorAppoinetmentAdapter(val mCallBack: ListSelector, val mcount: Int) :
    RecyclerView.Adapter<DoctorAppoinetmentAdapter.ViewHolder>() {
    public var itemList: ArrayList<Booking>? = null
    private var activity: BaseActivity? = null
    private var count: Int? = mcount

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val mBinding = DataBindingUtil.inflate<ViewMyBookingBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.view_my_booking, viewGroup, false
        )
        return ViewHolder(mBinding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        holder.mBinding.txtName.text = itemList!!.get(i).user_name
        holder.mBinding.txtTopic.text = itemList!!.get(i).topic
        holder.mBinding.txtTime.text = itemList!!.get(i).date+" "+itemList!!.get(i).time
        holder.itemView.setOnClickListener {
            mCallBack.selectedList(i)
        }

    }

    override fun getItemCount(): Int {
        return if (itemList == null) 0 else itemList!!.size

    }

    fun setUpcomingList(
        activity: BaseActivity,
        itemList: ArrayList<Booking>?
    ) {
        this.itemList = itemList
        this.activity = activity
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mBinding: ViewMyBookingBinding) :
        RecyclerView.ViewHolder(mBinding.root)

}