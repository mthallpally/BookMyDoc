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
import com.bookmydoc.interfaces.ListSelector

class SlotAdapter(val mCallBack: ListSelector, val mcount: Int) :
    RecyclerView.Adapter<SlotAdapter.ViewHolder>() {
    public var itemList: ArrayList<String>? = null
    private var activity: BaseActivity? = null
    private var count: Int? = mcount
    var row_index = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val mBinding = DataBindingUtil.inflate<ViewBookingTimeBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.view_booking_time, viewGroup, false
        )
        return ViewHolder(mBinding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        holder.mBinding.txtDate.text = itemList!!.get(i)
        holder.itemView.setOnClickListener {
            row_index = i
            mCallBack.selectedList(i)
            notifyDataSetChanged()
        }
        if (row_index == i) {

            holder.mBinding.txtDate.setTextColor(activity!!.getColor(R.color.white))
            holder.mBinding.txtDate.background =activity!!.getDrawable(R.drawable.rounded_blue_date)
        } else {
            holder.mBinding.txtDate.setTextColor(activity!!.getColor(R.color.txt_color))
            holder.mBinding.txtDate.background =activity!!.getDrawable(R.drawable.rounded_gray_border)
        }
    }

    override fun getItemCount(): Int {
        return if (itemList == null) 0 else itemList!!.size

    }

    fun setUpcomingList(
        activity: BaseActivity,
        itemList: ArrayList<String>?
    ) {
        this.itemList = itemList
        this.activity = activity
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mBinding: ViewBookingTimeBinding) :
        RecyclerView.ViewHolder(mBinding.root)

}