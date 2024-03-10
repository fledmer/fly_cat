package com.example.fly_cat

import android.media.MediaPlayer
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var imageView: ImageView
    private var catX: Double = 0.0
    private var catY: Double = 0.0
    private var speedX: Float = 0f
    private var speedY: Float = 0f
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var catWidth: Double = 0.0
    private var catHeight: Double = 0.0
    private var catAngle: Double = (Math.random() * 360)
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val gestureDetector by lazy { GestureDetector(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.catView)
        mediaPlayer = MediaPlayer.create(this, R.raw.cat_meow);

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        imageView.post {
            catWidth = imageView.width.toDouble()
            catHeight = imageView.height.toDouble()
            catX = (screenWidth - catWidth) / 2f
            catY = (screenHeight - catHeight) / 2f
            initCatMove()
        }
    }

    private fun initCatMove() {
        // Генерируем случайное направление движени

        catMove(8.0 )
    }

    private fun catMove(speed: Double){

        // Перемещаем котика
        catX += speed * cos(Math.toRadians(catAngle))
        catY += speed * sin(Math.toRadians(catAngle))

        // Отталкиваем котика от стен
        if (catX < 0 || catX + catWidth > screenWidth) {
            catAngle = computeReflectionAngle(catAngle, true)
            restartPlayer()
            speedX *= -1
            catX += speedX
        }
        if (catY < 0 || catY + catHeight > screenHeight) {
            catAngle = computeReflectionAngle(catAngle, false)
            restartPlayer()
            speedY *= -1
            catY += speedY
        }

        imageView.x = catX.toFloat()
        imageView.y = catY.toFloat()
        imageView.postDelayed({ catMove(speed) }, 10)
    }
    fun computeReflectionAngle(angle: Double, horizontalMovement: Boolean): Double {
        return if (horizontalMovement) {
            180f - angle
        } else {
            360f - angle
        }
    }
    fun restartPlayer(){
        mediaPlayer.stop()
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        imageView.removeCallbacks(null)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false;
    }

    override fun onShowPress(e: MotionEvent) {
        return
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false;
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null){
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        return
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        catAngle = Math.toDegrees(Math.atan2(-velocityY.toDouble(), -velocityX.toDouble()))
        return true
    }
}


