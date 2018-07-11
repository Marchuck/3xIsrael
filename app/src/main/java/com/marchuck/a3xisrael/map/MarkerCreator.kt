package com.marchuck.a3xisrael.map

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.marchuck.a3xisrael.R

object MarkerCreator {

    fun create(res: Resources, title: String, latLng: LatLng) = MarkerOptions()
            .icon(getBitmapDescriptor(res, R.drawable.ic_bomb))
            .title(title)
            .position(latLng)

    private fun getBitmapDescriptor(resources: Resources, @DrawableRes id: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}