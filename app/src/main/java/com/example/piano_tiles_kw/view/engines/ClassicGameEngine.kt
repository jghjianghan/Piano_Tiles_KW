package com.example.piano_tiles_kw.view.engines

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.example.piano_tiles_kw.view.UIThreadWrapper
import java.util.*
import kotlin.collections.ArrayList

/**
 * Manages a piano tile game in the given ImageView
 * @param context The context of the running game
 * @param iv The ImageView where the game will be displayed and played
 */
class ClassicGameEngine (
    private val context: Context,
    private val iv: ImageView,
    private var numberOfLanes:Int = 4
): GameEngine(context, iv), View.OnTouchListener {
    val handler = UIThreadWrapper(this, Looper.getMainLooper())
    val laneWidth = (iv.width.toFloat()) / numberOfLanes
    val laneCenters = ArrayList<Float>()
    val orchestrator = TileOrchestrator(
        handler,
        laneCenters,
        laneWidth,
        2000,
        10.toFloat(),
        iv.height.toFloat(),
        Color.BLACK
    )

    init {
        iv.setOnTouchListener(this)

        for (i in 1..numberOfLanes){
            laneCenters.add((i-0.5f)*laneWidth)
        }
        for (i in laneCenters){
            println(i)
        }

        initiatePaints()
        clearCanvas()
    }

    override fun initiatePaints() {
        strokePaint.color = Color.BLACK
        strokePaint.style = Paint.Style.STROKE
        fillPaint.color = Color.BLACK
    }

    /**
     * Resets the canvas' background and redraw its lane separator
     */
    override fun clearCanvas(){
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
     override fun redraw(drawers: ArrayList<TileDrawer>){
        clearCanvas()
        for (i in drawers){
            i.drawTile(mCanvas)
        }

        iv.invalidate()
    }

    override fun startGame(){
        println("game started")
        orchestrator.start()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val pointerIndex = event!!.actionIndex
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> Log.d("touch_listener", "down ${event.getX(pointerIndex)}, ${event.getY(pointerIndex)}")
            MotionEvent.ACTION_POINTER_DOWN -> Log.d("touch_listener", "pointer_down ${event.getX(pointerIndex)}, ${event.getY(pointerIndex)}")
            MotionEvent.ACTION_UP -> orchestrator.stop()
//            MotionEvent.ACTION_POINTER_UP -> Log.d("touch_listener", "pointer up")
//            MotionEvent.ACTION_MOVE -> Log.d("touch_listener", "move")
        }

        return true
    }
}