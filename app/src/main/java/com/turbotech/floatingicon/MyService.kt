package com.turbotech.floatingicon

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.VideoView
import androidx.annotation.RequiresApi

class MyService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private lateinit var uri: String
    lateinit var timer: CountDownTimer
    private var height = 100
    private var duration = 20000L

    //  private var weight = 0
    private var isLoopable = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        floatView()
        return START_STICKY
    }

    @SuppressLint("InflateParams")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun floatView() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        floatingView = inflater.inflate(R.layout.activity_float, null)

        timer = object : CountDownTimer(duration, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d("PlayRemainedTime", "Remaining Time: ${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                stopSelf()
                timer.cancel()
            }

        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
        params.x = 100
        params.y = 150

        windowManager?.addView(floatingView, params)

//      videoView from the activity_float.xml
        val floatingVideoView: VideoView? = floatingView?.findViewById(R.id.videoView)
        val layoutParams = floatingVideoView?.layoutParams
//      layoutParams?.width = width
        layoutParams?.height = height
        floatingVideoView?.minimumHeight = 100
        uri =
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4"
        floatingVideoView?.setVideoURI(Uri.parse(uri))

        floatingVideoView?.setOnInfoListener { _, what, _ ->
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                Log.d("PlayStatus", "Buffering")
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                Log.d("PlayStatus", "No Buffering")
            }
            true
        }

        floatingVideoView?.setOnPreparedListener { play ->
            play.isLooping = isLoopable
            timer.start()
            play.start()
            floatingVideoView.setOnClickListener {
                if (play.isPlaying) play.pause() else play.start()
            }
        }
        floatingVideoView?.setOnErrorListener { _, _, _ ->
            // Handle errors if needed
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let { windowManager?.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}