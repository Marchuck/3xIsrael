package com.marchuck.a3xisrael.map


import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.marchuck.a3xisrael.MainActivity
import com.marchuck.a3xisrael.R
import com.marchuck.a3xisrael.base.BaseFragment
import com.marchuck.a3xisrael.bomb.BombsPlacement
import java.util.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.content.res.ResourcesCompat
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor



class MapAroundFragment : BaseFragment<MainActivity>(), OnMapReadyCallback, MapAroundView {

    lateinit var finMeClicked: TextView

    internal var mapFragment: SupportMapFragment? = null
    internal val presenter: MapAroundPresenter by lazy { MapAroundPresenter() }
    var googleMap: GoogleMap? = null

    val random = Random()

    var boundsPlacement = BombsPlacement()

    @SuppressLint("MissingPermission")
    fun findMeClicked() {
        if (googleMap == null) {
            showGpsNotReady()
            setUpMapIfNeeded()
        } else {
            moveBomb(googleMap!!)
        }
//        if (googleMap != null) {
//            moveBomb(googleMap)
//
//            RxPermissions(getParentActivity())
//                    .request(Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_COARSE_LOCATION)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .flatMap {
//                        googleMap?.isMyLocationEnabled = true
//                        val myLocation = googleMap?.myLocation
//                        return@flatMap Observable.just(myLocation)
//                    }
//                    .retryWhen(RetryWithDelay(3, 300))
//                    .subscribe({ onReceivedLocation(it!!) }, { onPlaceReceiveError(); });
//        } else {
//            showGpsNotReady()
//            setUpMapIfNeeded()
//        }
    }

    override fun onReceivedLocation(location: Location) {
        Handler(Looper.getMainLooper())
                .post(Runnable {
                    if (googleMap == null) {
                        return@Runnable
                    }

                    val locationName = location.provider.trim { it <= ' ' }

                    val pos = LatLng(location.latitude, location.longitude)
                    googleMap!!.clear()
                    googleMap!!.moveCamera(CameraUpdateFactory
                            .newCameraPosition(CameraPosition(pos, 16f, 60f, Random().nextInt(360).toFloat())))

                    googleMap!!.addMarker(
                            MarkerOptions().alpha(0.7f).title(locationName)
                                    .position(pos)
                    )

                    if (!locationName.isEmpty())
                        showMessage(message = "Your location: $locationName")
                })
    }

    override fun showGpsNotReady() {
        finMeClicked.post({ showMessage("Enable GPS in order to fetch location.") })
    }

    override fun onPlaceReceiveError() {
        finMeClicked.post({ showMessage("Failed get last location") })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, sis: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_map_around, container, false)

        finMeClicked = contentView.findViewById(R.id.bottom_sheet_map_button)
        finMeClicked.setOnClickListener { findMeClicked() }
        presenter.view = (this)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapIfNeeded()
    }

    internal fun setUpMapIfNeeded() {

        if (mapFragment == null) {
            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        }

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        moveBomb(googleMap)
    }

    private fun moveBomb(googleMap: GoogleMap) {
        googleMap.clear()

        boundsPlacement = boundsPlacement.pushRandom(random)

        for ((a, latLng) in boundsPlacement.locations.withIndex()) {
            googleMap.addMarker(MarkerOptions()
                    .icon(getBitmapDescriptor(R.drawable.ic_bomb))
                    .title("${a + 1}")
                    .position(latLng))
        }
        googleMap.setLatLngBoundsForCameraTarget(boundsPlacement.bounds)
    }

    private fun getBitmapDescriptor(@DrawableRes id: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        val TAG = MapAroundFragment::class.java.simpleName

        fun newInstance(): MapAroundFragment {
            return MapAroundFragment()
        }
    }

}
