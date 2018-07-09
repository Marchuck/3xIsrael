package com.marchuck.a3xisrael.map

import android.Manifest
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.marchuck.a3xisrael.MainActivity
import com.marchuck.a3xisrael.R
import com.marchuck.a3xisrael.base.BaseFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

import java.util.Random

class MapAroundFragment : BaseFragment<MainActivity>(), OnMapReadyCallback, MapAroundView {

    lateinit var finMeClicked: TextView

    internal val mapFragment: SupportMapFragment by lazy { SupportMapFragment() }
    internal val presenter: MapAroundPresenter by lazy { MapAroundPresenter() }
    internal var googleMap: GoogleMap? = null

    @SuppressLint("MissingPermission")
    fun findMeClicked() {

        if (googleMap != null) {

            RxPermissions(getParentActivity())
                    .request(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap {
                        googleMap?.isMyLocationEnabled = true
                        val myLocation = googleMap?.myLocation
                        return@flatMap Observable.just(myLocation)
                    }
                    .retryWhen(RetryWithDelay(3, 300))
                    .map({ location ->
                        presenter.fetchLocation(location, RxGeoCoder(activity));
                        return@map true
                    })
                    .subscribe({ it }, { onPlaceReceiveError(); });
        } else {
            showGpsNotReady()
            setUpMapIfNeeded()
        }
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
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    companion object {
        val TAG = MapAroundFragment::class.java.simpleName

        fun newInstance(): MapAroundFragment {
            return MapAroundFragment()
        }
    }

}
