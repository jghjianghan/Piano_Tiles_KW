package com.example.piano_tiles_kw.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.piano_tiles_kw.R
import java.util.concurrent.ThreadLocalRandom

/**
 * Manages a piano tile game in the given ImageView
 * @param context The context of the running game
 * @param iv The ImageView where the game will be displayed and played
 */
class GameEngine (private val context: Context, private val iv: ImageView, private var numberOfLanes:Int = 4): View.OnTouchListener {
    private val mBitmap: Bitmap = Bitmap.createBitmap(
        iv.width,
        iv.height,
        Bitmap.Config.ARGB_8888
    )
    private val mCanvas = Canvas(mBitmap)
    private val strokePaint = Paint()
    private val fillPaint = Paint()
    val laneWidth = (iv.width.toFloat()) / numberOfLanes
    val laneCenters = ArrayList<Float>()
    val tiles = ArrayList<Tile>()

    init {
        iv.setImageBitmap(mBitmap)
        iv.setOnTouchListener(this)

        for (i in 1..numberOfLanes){
            laneCenters.add((i-0.5f)*laneWidth)
        }
        for (i in laneCenters){
            println(i)
        }

        strokePaint.color = Color.BLACK
        strokePaint.style = Paint.Style.STROKE
        clearCanvas()
        fillPaint.color = Color.BLACK
    }

    /**
     * Resets the canvas' background and redraw its lane separator
     */
    private fun clearCanvas(){
        mCanvas.drawColor(Color.WHITE)

        for (i in 1 until numberOfLanes){
            mCanvas.drawLine(
                i*laneWidth.toFloat(),
                0f,
                i*laneWidth.toFloat(),
                iv.height.toFloat(),
                strokePaint)
        }

        iv.invalidate()
    }

    /**
     * Resets the canvas and then redraw all the elements (tiles)
     */
    private fun redraw(){
        clearCanvas()
        for (i in tiles){
            i.drawTile(mCanvas)
        }

        iv.invalidate()
    }

    fun startGame(){
        println("starting")
        tiles.clear()
        for (i in laneCenters){
            tiles.add(Tile(laneWidth, laneWidth*1.6f, i, 10f, Color.BLACK))
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val pointerIndex = event!!.actionIndex
        when(event!!.actionMasked){
            MotionEvent.ACTION_DOWN -> Log.d("touch_listener", "down ${event.getX(pointerIndex)}, ${event.getY(pointerIndex)}")
            MotionEvent.ACTION_POINTER_DOWN -> Log.d("touch_listener", "pointer_down ${event.getX(pointerIndex)}, ${event.getY(pointerIndex)}")
            MotionEvent.ACTION_UP -> startGame()
//            MotionEvent.ACTION_POINTER_UP -> Log.d("touch_listener", "pointer up")
//            MotionEvent.ACTION_MOVE -> Log.d("touch_listener", "move")

        }
        return true
    }
}