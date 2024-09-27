package com.example.sample

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SafePlacesActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private val minTime: Long = 1000
    private val minDist: Float = 5f
    private val tomTomApiKey = "t1SiY9sCprubVRISWoLrNY3da51jT4fx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Request permissions
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Location listener to get the current location
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val newLatLng = LatLng(location.latitude, location.longitude)
                mMap.clear() // Clear previous markers
                mMap.addMarker(MarkerOptions().position(newLatLng).title("My Position"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))

                // Fetch nearby police stations
                fetchNearbyPoliceStations(location.latitude, location.longitude)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        // Location manager to request updates
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDist, locationListener)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun fetchNearbyPoliceStations(latitude: Double, longitude: Double) {
        // Adjust the radius to 500 meters
        val radiusMeters = 5000
        val url = "https://api.tomtom.com/search/2/search/police.json?key=$tomTomApiKey&lat=$latitude&lon=$longitude&radius=$radiusMeters"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to fetch police stations", Toast.LENGTH_SHORT).show()
                    }
                    return@Thread
                }

                val responseData = response.body?.string()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData)
                    val results = jsonObject.getJSONArray("results")

                    runOnUiThread {
                        mMap.clear()  // Clear previous markers

                        for (i in 0 until results.length()) {
                            val place = results.getJSONObject(i)
                            val poi = place.optJSONObject("poi") // Ensure 'poi' object is not null
                            if (poi != null) {
                                val name = poi.optString("name", "Unnamed Police Station") // Provide a fallback name if no name is found
                                val latLng = LatLng(
                                    place.getJSONObject("position").getDouble("lat"),
                                    place.getJSONObject("position").getDouble("lon")
                                )

                                // Add a blue marker with the police station name
                                val markerOptions = MarkerOptions()
                                    .position(latLng)
                                    .title(name)  // Set the title for the marker
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))  // Set marker color to blue

                                mMap.addMarker(markerOptions)
                            }
                        }
                        // Move the camera to the current location with a zoom level of 16 to ensure the area is visible
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16f))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }







    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
