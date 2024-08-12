package com.turbotech.floatingicon

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.turbotech.floatingicon.databinding.ActivityFloatBinding

class FloatActivity : AppCompatActivity() {
    private lateinit var floatActivityMainBinding: ActivityFloatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        floatActivityMainBinding = ActivityFloatBinding.inflate(layoutInflater)
        val view = floatActivityMainBinding.root
        enableEdgeToEdge()
        setContentView(view)
    }
}