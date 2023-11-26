
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//Not commented because it is very likely to change
class LocationHelper(private val context: Context, private val locationCallback: (Location) -> Unit) {

    private lateinit var locationManager: LocationManager

    fun requestLocationUpdates() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_INTERVAL,
                    LOCATION_UPDATE_MIN_DISTANCE,
                    locationListener
                )
            } else {
                showLocationSettingsDialog()
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationCallback.invoke(location)

            locationManager.removeUpdates(this)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    private fun showLocationSettingsDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enable Location Services")
            .setMessage("Please enable GPS or network location services.")
            .setPositiveButton("OK") { _, _ ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()
            .show()
    }

    companion object {
        private const val LOCATION_UPDATE_INTERVAL: Long = 1000
        private const val LOCATION_UPDATE_MIN_DISTANCE: Float = 10f
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}
