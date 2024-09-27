package com.example.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private val minTime: Long = 1000
    private val minDist: Float = 5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Request permissions
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), PERMISSION_REQUEST_CODE)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                try {
                    val newLatLng = LatLng(location.latitude, location.longitude)

                    // Update the map with the new location
                    mMap.clear() // Clear previous markers
                    mMap.addMarker(MarkerOptions().position(newLatLng).title("My Position"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng))

                    // Prepare the SMS content with an accurate Google Maps link
                    val phoneNumber = "8590646472"
                    val mapsLink = "https://www.google.com/maps?q=${location.latitude},${location.longitude}"
                    val message = "Here is my current location: $mapsLink"

                    // Ensure permissions are granted
                    if (ActivityCompat.checkSelfPermission(this@MapsActivity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        // Use an intent to open the SMS app
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:$phoneNumber")
                            putExtra("sms_body", message)
                        }
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MapsActivity, "No SMS app found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MapsActivity, "SMS permission not granted", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                // Deprecated, can be removed if not used
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Handle the case where permissions are not granted
                return
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDist, locationListener)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDist, locationListener)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // Handle the result of the permissions request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
