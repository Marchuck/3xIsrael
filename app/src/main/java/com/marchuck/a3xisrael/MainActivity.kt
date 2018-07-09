package com.marchuck.a3xisrael

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.marchuck.a3xisrael.map.MapAroundFragment
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionThenShowMap(savedInstanceState)
    }

    private fun requestPermissionThenShowMap(savedInstanceState: Bundle?) {
        RxPermissions(this).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe {
                    if (it) {
                        if (savedInstanceState == null) {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_content, MapAroundFragment.newInstance())
                                    .commitAllowingStateLoss()
                        }
                    } else {
                        AlertDialog.Builder(MainActivity@ this)
                                .setTitle("Permission needed")
                                .setMessage("Location permission is needed to use the maps feature")
                                .setPositiveButton("OK", { dialog, v ->
                                    dialog.dismiss()
                                    requestPermissionThenShowMap(savedInstanceState)
                                })
                                .setNegativeButton("EXIT APP", { dialog, v ->
                                    dialog.dismiss()
                                    finish()
                                }).show()
                    }
                }
    }
}
