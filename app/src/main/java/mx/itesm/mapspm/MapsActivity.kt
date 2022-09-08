package mx.itesm.mapspm

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.itesm.mapspm.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERMISO_UBICACION = 0
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // AS - CASTING
        // cambio arbitrario de tipo
        // OJO - si se trata de hacer casting a un tipo no representable
        // tendremos una excepción en ejecución
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val salon = LatLng(20.734797, -103.457287)
        mMap.addMarker(MarkerOptions().position(salon).title("SALON"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salon, 18f))

        mMap.setOnMapClickListener { latLng ->

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("marcador creado dinámicamente")
                    .alpha(0.5f)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE
                        )
                    )
            )
        }

        habilitarMyLocation()
    }

    fun habilitarMyLocation() {

        // checar si tenemos permiso
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            // si el permiso está dado corremos funcionalidad
            mMap.isMyLocationEnabled = true
        } else {

            // y si no pues pedimos permiso

            // enlistamos los permisos a solicitar en un arreglo
            val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permisos, PERMISO_UBICACION)
        }
    }


    // método para escuchar respuesta a solicitud de permiso
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // checamos si el origen es el de la ubicación y si el permiso fue dado
        if(requestCode == PERMISO_UBICACION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        }
    }
}