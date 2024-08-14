package com.turbotech.floatingicon

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.turbotech.floatingicon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var requestPermission: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        if (Settings.canDrawOverlays(this)) {
            // Permission granted
            Toast.makeText(
                this@MainActivity,
                "Overlay permission already granted",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Register the permission request launcher
            requestPermission = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
               Log.d("PermissionStatus","No Permission")
            }
            // Launch the settings page for overlay permission
            requestPermission.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        }

        viewBinding.actualFloatingButton.setOnClickListener {
            startService(Intent(this@MainActivity, MyService::class.java))
            if (viewBinding.invisibleFloatingButton.isVisible) viewBinding.invisibleFloatingButton.hide() else viewBinding.invisibleFloatingButton.show()
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this@MainActivity, MyService::class.java))
    }
}

