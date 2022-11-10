package com.bookmydoc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmydoc.R
import com.bookmydoc.adapter.DoctorAdapter
import com.bookmydoc.base.BaseActivity
import com.bookmydoc.databinding.ActivityDashboardBinding
import com.bookmydoc.databinding.ActivityDoctorListBinding
import com.bookmydoc.interfaces.ListSelector
import java.util.ArrayList

class DoctorListActivity : BaseActivity(), View.OnClickListener {

    private var doctorAdapyter: DoctorAdapter? = null
    private var doctorArrayList = ArrayList<String>()
    private lateinit var binding: ActivityDoctorListBinding
    var categoryName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_list)
        binding.imgBack.setOnClickListener(this)
        categoryName = intent.getStringExtra("category").toString()
        binding.txtTitle.text = categoryName
        doctorAdapyter = DoctorAdapter(object : ListSelector {
            override fun selectedList(position: Int) {

                val intent = Intent(this@DoctorListActivity, DocotrDetailActivity::class.java)
                intent.putExtra("drName", doctorArrayList.get(position))
                startActivity(intent)
            }

        }, 3)
        binding.rvMorningSlot.layoutManager = LinearLayoutManager(this)
        binding.rvMorningSlot.adapter = doctorAdapyter

        doctorArrayList.add("Dr. TITUS CARR EVANS")
        doctorArrayList.add("Dr. DAVID H. HOCH")
        doctorArrayList.add("Dr. PETER M OKIN")
        doctorArrayList.add("Dr. ALLAN S JAFFE")
        doctorArrayList.add("Dr. ELLIS G REEF")
        doctorArrayList.add("Dr. ELSA GRACE GIARDINA")
        doctorArrayList.add("Dr. WILLIAM E LAWSON")
        doctorArrayList.add("Dr. JOHN S PANTAZOPOULOS")
        doctorArrayList.add("Dr. DAVID B WILSON")
        doctorArrayList.add("Dr. MARY ANN PEBERDY")
        doctorArrayList.add("Dr. THOMAS RANDOLPH FLIPSE")

        doctorAdapyter!!.setUpcomingList(this, doctorArrayList)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imgBack -> {
                onBackPressed()
            }
        }
    }
}