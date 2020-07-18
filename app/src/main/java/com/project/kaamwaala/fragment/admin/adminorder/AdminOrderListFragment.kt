package com.project.kaamwaala.fragment.admin.adminorder


import `in`.akshit.horizontalcalendar.HorizontalCalendarView
import `in`.akshit.horizontalcalendar.Tools
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.CalandarAdapter
import com.project.kaamwaala.adapter.admin.customer.AdminOrderListAdapter
import com.project.kaamwaala.model.CalendarModel
import com.project.kaamwaala.model.admin_order.AdminOrderListRequest
import com.project.kaamwaala.model.admin_order.AdminOrderListResponse
import com.project.kaamwaala.network.PrefsConstants

import kotlinx.android.synthetic.main.fragment_admin_tablayout.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein

import java.text.SimpleDateFormat

import java.util.*


class AdminOrderListFragment: Fragment() ,CalandarAdapter.OnItemAction, KodeinAware {
    override val kodein by kodein()
    private val factory: AdminOrderVMF by instance<AdminOrderVMF>()
    private lateinit var viewmodel: AdminOrderVM
    var list: MutableList<String> = mutableListOf()
    lateinit var start: Calendar
    lateinit var callback: CalandarAdapter.OnItemAction
    lateinit var calenderAdapter: CalandarAdapter
    lateinit var listTitle: MutableList<String>

    var days = arrayOf(
        "Sun",
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat"
    )
    var year = arrayOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "June",
        "July",
        "Aug",
        "Sept",
        "Oct",
        "Nov",
        "Dec"
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View=inflater.inflate(R.layout.fragment_admin_tablayout, container, false)

        return  view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewmodel = ViewModelProviders.of(this, factory).get(AdminOrderVM::class.java)


        val starttime = Calendar.getInstance()
        starttime.add(Calendar.MONTH, -6)

        val endtime = Calendar.getInstance()
        endtime.add(Calendar.MONTH, 6)

        val sdf = SimpleDateFormat("dd-MMM-yyyy")

        val datesToBeColored = ArrayList<String>()
        datesToBeColored.add(Tools.getFormattedDateToday())

        calendar.setUpCalendar(starttime.timeInMillis,
            endtime.timeInMillis,
            datesToBeColored,
            object : HorizontalCalendarView.OnCalendarListener {
                override fun onDateSelected(date: String) {
                    // Toast.makeText(activity, "$date clicked!", Toast.LENGTH_SHORT).show()
                }
            })


        callback = this

        listTitle = mutableListOf()


        initCal()

        btnNext.setOnClickListener { loadNext() }
        btnPrevious.setOnClickListener {
            loadPrevious()
        }



    }


    private fun initCal() {
        start = Calendar.getInstance()
        start.add(Calendar.DATE, -3)
        plotCalendar(start)

    }

    private fun plotCalendar(start: Calendar) {
        listTitle.clear()
        val cal = start.clone() as Calendar
        val list = mutableListOf<CalendarModel>()
        for (i in 1..7) {
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
            cal.get(Calendar.YEAR)
            cal.get(Calendar.MONTH)
            cal.get(Calendar.DAY_OF_MONTH)
            var   text:String = sdf.format(cal.time)

            val model = CalendarModel(cal.get(Calendar.DATE).toString(), days[cal.get(Calendar.DAY_OF_WEEK) - 1], cal, false,text)
            if (i == 4)
                model.isSelected = true



            Log.v("dfhfgfgfg",text)

            AdminOrderListApi(AdminOrderListRequest("OrderList",Prefs.getString(PrefsConstants.UserId,""),text,""))

            val title = year[cal.get(Calendar.MONTH)]
            if (!listTitle.contains(title))
                listTitle.add(title)

            list.add(model)
            cal.add(Calendar.DATE, +1)
        }

        var title = ""
        for (i in 0 until listTitle.size) {

            if (i==listTitle.size-1)
            {
                title += listTitle[i]
            }
            else
            {
                title += listTitle[i] + "/"
            }
        }

        titlee.text = title+ " " + cal.get(Calendar.YEAR)

        calenderAdapter = CalandarAdapter(list, callback)
        rvCalander.apply {
            layoutManager = GridLayoutManager(activity, 7)
            adapter = calenderAdapter
        }
    }

    private fun loadNext() {
        start.add(Calendar.DATE, +7)
        plotCalendar(start)
    }

    private fun loadPrevious() {
        start.add(Calendar.DATE, -7)
        plotCalendar(start)
    }

    override fun onItemSelect(model: CalendarModel,date:String) {
        //on date selected



        Log.v("dfhfgfgfg",date)

        AdminOrderListApi(AdminOrderListRequest("OrderList",Prefs.getString(PrefsConstants.UserId,""),date,""))

    }


    //=====================================Admin================================================//
    private fun AdminOrderListApi(adminStallListRequest: AdminOrderListRequest) {
        viewmodel?.responseOrderList = MutableLiveData()
        viewmodel?.responseOrderList?.observe(this, Observer {

            val response = (it as AdminOrderListResponse)
            if (response.Status) {
                rvAdminOrder.apply {
                    rvAdminOrder.layoutManager = LinearLayoutManager(context)
                    adapter = AdminOrderListAdapter(response.StoreOrderList) }
            }
            else {
                rvAdminOrder.apply {
                    rvAdminOrder.layoutManager = LinearLayoutManager(context)
                    adapter = AdminOrderListAdapter(response.StoreOrderList) }

            }

        })
        viewmodel.adminorderlist(adminStallListRequest)
    }



}


