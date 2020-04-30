package edu.upenn.cis350.irally.ui.location

import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.location.*
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.load.loadEventInfo
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_location.*
import org.json.JSONObject

class LocationActivity : AppCompatActivity() {

    private val permissionID = 42
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    companion object {
        var contexTT: Context? = null
        private lateinit var contextT: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        contextT = this
        contexTT = this

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()


        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar)
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val positionObject = JSONObject()
                        positionObject.put("lat", location.latitude)
                        positionObject.put("lng", location.longitude)

                        val eventRequestBody = JSONObject()
                        eventRequestBody.put("userPosition", positionObject)

                        val eventJsonObjectRequest = JsonObjectRequest(
                            "http://10.0.2.2:9000/events/getClosestEvents/",
                            eventRequestBody,
                            Response.Listener { response ->
                                Log.v("PROCESS", "got a response (register")
                                Log.v("RESPONSE", response.toString())
                                if (response.getString("status") == "Success") {
                                    Log.v("Response Success", "Event received")
                                    val eventJSONList = response.getJSONArray("closestEvents")
                                    if (eventJSONList.length() == 0) {
                                        near_event1.text =
                                            getString(R.string.no_events_found_nearby)
                                    } else {
                                        for (i in 0 until eventJSONList.length()) {
                                            val eventText = eventJSONList.get(i).toString()
                                            Log.v("clossest vevents", eventText)
                                            if (i == 0) {
                                                near_event1.text = eventText
                                                near_event1.setOnClickListener {
                                                    loadEventInfo(
                                                        it,
                                                        eventText,
                                                        this,
                                                        applicationContext
                                                    )
                                                }
                                            } else {
                                                val myButton = Button(this)
                                                myButton.text = eventText
                                                myButton.setOnClickListener {
                                                    loadEventInfo(
                                                        it,
                                                        eventText,
                                                        this,
                                                        applicationContext
                                                    )
                                                }
                                                event_near_layout.addView(myButton)
                                            }
                                        }
                                    }
                                } else {
                                    Log.v("Response Success", "Events not found")
                                    Log.v("ERROR", response.getString("errors"))
                                    Toast.makeText(
                                        applicationContext,
                                        "Unable to load nearby events: ${response.getString("errors")}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            Response.ErrorListener { error ->
                                Log.e("Response Error", "Error occurred", error)
                                Toast.makeText(
                                    applicationContext,
                                    "Unable to load nearby events: ${error.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )

                        RequestQueueSingleton.getInstance(LoginActivity.context)
                            .addToRequestQueue(eventJsonObjectRequest)

                        //
                        //                        Response.ErrorListener { error ->
                        //                            Toast.makeText(
                        //                                applicationContext,
                        //                                ("Network connection error. Please try again. " +
                        //                                        "Error: %s").format(error.toString()),
                        //                                Toast.LENGTH_LONG
                        //                            ).show()
                        //
                        //                        }
                        //                    )

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
//            latTextView.text = mLastLocation.latitude.toString()
//            lonTextView.text = mLastLocation.longitude.toString()
            val positionObject = JSONObject()
            positionObject.put("lat", mLastLocation.latitude)
            positionObject.put("lng", mLastLocation.longitude)

            val eventRequestBody = JSONObject()
            eventRequestBody.put("userPosition", positionObject)

            val eventJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/events/getClosestEvents/",
                eventRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        Log.v("Response Success", "Event received")
                        val eventJSONList = response.getJSONArray("closestEvents")
                        if (eventJSONList.length() == 0) {
                            near_event1.text =
                                getString(R.string.no_events_found_nearby)
                        } else {
                            for (i in 0 until eventJSONList.length()) {
                                val eventText = eventJSONList.get(i).toString()
                                Log.v("clossest vevents", eventText)
                                if (i == 0) {
                                    near_event1.text = eventText
                                    near_event1.setOnClickListener {
                                        loadEventInfo(
                                            it,
                                            eventText,
                                            contexTT!!,
                                            applicationContext
                                        )
                                    }
                                } else {
                                    val myButton = Button(contexTT!!)
                                    myButton.text = eventText
                                    myButton.setOnClickListener {
                                        loadEventInfo(
                                            it,
                                            eventText,
                                            contexTT!!,
                                            applicationContext
                                        )
                                    }
                                    event_near_layout.addView(myButton)
                                }
                            }
                        }
                    } else {
                        Log.v("Response Success", "Events not found")
                        Log.v("ERROR", response.getString("errors"))
                        Toast.makeText(
                            applicationContext,
                            "Unable to load nearby events: ${response.getString("errors")}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to load nearby events: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(eventJsonObjectRequest)

            //
            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again. " +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
            //
            //                        }
            //                    )

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


    fun goToProfile(v: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun goToFeed(v: View) {
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(v: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
