package com.example.earthquick
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.coroutines.coroutineContext

private const val TAG = "Erth"

class EarthQuickFragment :Fragment(){

    private lateinit var erthQuickRecyclerView: RecyclerView
    private lateinit var erthQuickViewModel: EarthQuickViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        erthQuickViewModel = ViewModelProviders.of(this).get(EarthQuickViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragement_earthquick, container, false)
        erthQuickRecyclerView = view.findViewById(R.id.earth_recycler_view)
        erthQuickRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        erthQuickViewModel.earthItemsLiveData.observe(
            viewLifecycleOwner,
            Observer {
                    erthItems ->
                Log.d(TAG, "Have erth items from ViewModel $erthItems")
                erthQuickRecyclerView.adapter = EarthQuickAdapter(erthItems)
            })
    }
    companion object {
        @JvmStatic
        fun newInstance() = EarthQuickFragment()
    }
    private  inner class EarthQuickHolder(itemTextView: View ) : RecyclerView.ViewHolder(itemTextView) ,View.OnClickListener {
        lateinit var coordinate: List<Double>
        val placeTextView = itemTextView.findViewById(R.id.place) as TextView
        val countryTextView = itemTextView.findViewById(R.id.country) as TextView
        val magTextView = itemTextView.findViewById(R.id.mag) as TextView
        val dateTextView = itemTextView.findViewById(R.id.date) as TextView
        val timeTextView = itemTextView.findViewById(R.id.time) as TextView

        init {
            itemTextView.setOnClickListener(this)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(earthdate: EarthData) {
            var time = milliscondToTime(earthdate.properties.time)
            var date = getDate(earthdate.properties.time, "MMM DD, yyyy")
            dateTextView.text=date
            timeTextView.text=time
            val string = earthdate.properties.place
            val parts = string.split("of".toRegex()).toTypedArray()
            val address = parts[0] // 2km NNW
            val country = parts[1] // Palomar Observatory
            placeTextView.setText(country)
            countryTextView.setText(address)
            var mag = earthdate.properties.mag

            magTextView.setText(mag.toString())
            when {
                mag in 2.0..3.9 -> magTextView.setBackgroundResource(R.drawable.round_shape)
                mag in 4.0..4.9 -> magTextView.setBackgroundResource(R.drawable.round_yellow_shape)
                mag in 5.0..5.9 -> magTextView.setBackgroundResource(R.drawable.round_orange_shape)
                mag in 6.0..10.0 -> magTextView.setBackgroundResource(R.drawable.round_red_shape)
                else -> magTextView.setBackgroundResource(R.drawable.round_shape)
            }
            coordinate = earthdate.geometry.coordinates
        }
        override fun onClick(p0: View?) {
            var longitude= coordinate[0]
            var  latitude=coordinate[1]
            val mapIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("geo:$longitude,$latitude")
            }
            ContextCompat.startActivity(itemView.context, mapIntent, null)
        }
        @RequiresApi(Build.VERSION_CODES.N)
        private fun formatMag(mag: Double): String? {
            val magFormat = DecimalFormat("0.0")
            return magFormat.format(mag)
        }
        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.N)
        fun milliscondToTime(Datemilli: Long) : String {
            val timeInMilliseconds = Datemilli
            val dateObject = Date(timeInMilliseconds)
            val timeFormatter = SimpleDateFormat("hh:mm a")
            val timeToDisplay: String = timeFormatter.format(dateObject)
            return timeToDisplay
        }


        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.N)
        fun getDate(milliSeconds: Long, dateFormat: String?): String? {
            var formatter = SimpleDateFormat(dateFormat)
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }



    }
    private inner class EarthQuickAdapter(private val erthItems: List<EarthData>)
        : RecyclerView.Adapter<EarthQuickHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EarthQuickHolder {
            val v=  LayoutInflater.from(parent.context).inflate(
                R.layout.earthquick_details,
                parent,
                false
            )
            return EarthQuickHolder(v)
        }
        override fun getItemCount(): Int = erthItems.size
        //  @RequiresApi(Build.VERSION_CODES.N)
        override fun onBindViewHolder(holder: EarthQuickHolder, position: Int) {
            val erthItems = erthItems[position]
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.bind(erthItems)
            }
        }
    }


}