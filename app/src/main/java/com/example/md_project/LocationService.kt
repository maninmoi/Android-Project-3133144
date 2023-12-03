
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.md_project.LocationViewModel


class LocationService(private val context: Context, private val locationViewModel: LocationViewModel) {

    private var locationManager: LocationManager? = null
    private lateinit var locationListener: LocationListener


    fun startLocationUpdates() {
        if (checkLocationPermission()) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationViewModel.updateLocation(location.latitude, location.longitude) //Viewmodel values get updated
                }
            }

            //Registers for location updates
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener
            )
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private const val MIN_TIME_BETWEEN_UPDATES: Long = 1000 //Time between updates in milliseconds

        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f //Minimum distance from the old location to trigger a update (in meters)

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1 //Request code for the location permission
    }
}




