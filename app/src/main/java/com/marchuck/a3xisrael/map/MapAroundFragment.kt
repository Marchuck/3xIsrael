package com.marchuck.a3xisrael.map


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.marchuck.a3xisrael.MainActivity
import com.marchuck.a3xisrael.R
import com.marchuck.a3xisrael.api.NukeDirection
import com.marchuck.a3xisrael.base.BaseFragment
import com.marchuck.a3xisrael.bomb.BombsPlacement
import com.marchuck.a3xisrael.moveNuke.MoveNukeListener
import pl.marchuck.cryptoapp.receive.chooseAccount.MoveNukeBottomDialog
import java.util.*


class MapAroundFragment : BaseFragment<MainActivity>(), OnMapReadyCallback, MapAroundView, MoveNukeListener {

    lateinit var finMeClicked: TextView

    internal var mapFragment: SupportMapFragment? = null

    internal var lastIndexInUse: Int? = null

    internal val presenter: MapAroundPresenter by lazy { MapAroundPresenter() }

    var googleMap: GoogleMap? = null

    @SuppressLint("MissingPermission")
    fun findMeClicked() {
//        Toast.makeText(context, "Feature not ready", Toast.LENGTH_SHORT).show()
        presenter.singleShot()
    }

    override fun onReceivedNukes(nukes: ArrayList<LatLng>?) {
        if (googleMap == null) {
            return
        }

        val boundsPlacement = BombsPlacement(nukes)

        googleMap?.clear()

        for ((a, latLng) in boundsPlacement.locations.withIndex()) {
            googleMap?.addMarker(MarkerCreator.create(resources, "$a", latLng))
        }
        googleMap?.setLatLngBoundsForCameraTarget(boundsPlacement.bounds)
    }

    override fun showGpsNotReady() {
        finMeClicked.post({ showMessage("Enable GPS in order to fetch location.") })
    }

    override fun onPlaceReceiveError() {
        finMeClicked.post({ showMessage("Failed get nukes") })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, sis: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_map_around, container, false)

        finMeClicked = contentView.findViewById(R.id.bottom_sheet_map_button)
        finMeClicked.setOnClickListener { findMeClicked() }
        presenter.view = this
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
        googleMap.setOnMarkerClickListener { marker ->
            val title = marker.title

            presenter.requestMoveNuke(Integer.valueOf(title))

            return@setOnMarkerClickListener true
        }
        presenter.startNukesInterval()
    }

    override fun showMoveNukeDialog(index: Int?) {
        lastIndexInUse = index
        val dialog = MoveNukeBottomDialog()
        dialog.show(getParentActivity().supportFragmentManager, dialog.TAG)

    }


    override fun onResume() {
        super.onResume()
        if (googleMap != null) {
            presenter.startNukesInterval()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.stopNukesInterval()
    }

    override fun onMoveNuke(direction: NukeDirection) {
        println("onMoveNuke $direction")
        presenter.moveNuke(lastIndexInUse, direction)
    }

    override fun showPlacedNuke() {
        finMeClicked.post({ showMessage("Nuke moved!") })
    }

    override fun onPlaceNukeError() {
        finMeClicked.post({ showMessage("Failed to move nuke, try again later") })
    }


    companion object {
        val TAG = MapAroundFragment::class.java.simpleName

        fun newInstance(): MapAroundFragment {
            return MapAroundFragment()
        }
    }

}
