package com.example.piano_tiles_kw.view.engines.raining

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Looper
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.piano_tiles_kw.R
import com.example.piano_tiles_kw.view.UIThreadWrapper
import com.example.piano_tiles_kw.view.engines.GameEngine
import com.example.piano_tiles_kw.view.engines.TileDrawer
import kotlin.collections.ArrayList

/**
 * Manages a piano tile game in the given ImageView
 * @param context The context of the running game
 * @param iv The ImageView where the game will be displayed and played
 */
class RainingGameEngine(
    private val context: Context,
    private val iv: ImageView,
    private var numberOfLanes: Int = 4
) : GameEngine(context, iv), View.OnTouchListener {
    val handler = UIThreadWrapper(this, Looper.getMainLooper())
    val laneWidth = (iv.width.toFloat()) / numberOfLanes
    val laneCenters = ArrayList<Float>()
    val scoreTextSize = 24
    val textPaint = TextPaint()
    val orchestrator = RainingTileOrchestrator(
        this,
        handler,
        laneCenters,
        laneWidth,
        2000,
        10.toFloat(),
        iv.height.toFloat(),
        Color.BLACK
    )
    var isOver = false

    init {
        iv.setOnTouchListener(this)

        for (i in 1..numberOfLanes) {
            laneCenters.add((i - 0.5f) * laneWidth)
        }
        for (i in laneCenters) {
            println(i)
        }

        initiatePaints()
        clearCanvas()
    }

    override fun initiatePaints() {
        super.initiatePaints()
        textPaint.color = ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null)
        textPaint.textSize = scoreTextSize * context.resources.displayMetrics.density
    }

    /**
     * Resets the canvas' background and redraw its lane separator
     */
    override fun clearCanvas() {
        mCanvas.drawColor(Color.WHITE)

        for (i in 1 until numberOfLanes) {
            mCanvas.drawLine(
                i * laneWidth.toFloat(),
                0f,
                i * laneWidth.toFloat(),
                iv.height.toFloat(),
                strokePaint
            )
        }
    }

    /**
     * Resets the canvas and then redraw all the elements (tiles)
     */
    override fun redraw(drawers: ArrayList<TileDrawer>) {
        clearCanvas()
        for (i in drawers) {
            i.drawTile(mCanvas)
        }

        val score = getScore().toString()
        val width = textPaint.measureText(score)
        StaticLayout(
            score,
            textPaint,
            width.toInt(),
            Layout.Alignment.ALIGN_NORMAL,
            1f,
            0f,
            false
        ).draw(mCanvas)

        println("score " + getScore())

        iv.invalidate()
    }

    override fun startGame() {
        println("game started")
        orchestrator.start()
    }

    override fun stopGame() {
        if (!isOver){
            isOver = true
            orchestrator.stop()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (!isOver) {
            val pointerIndex = event!!.actionIndex
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(
                        "touch_listener",
                        "down ${event.getX(pointerIndex)}, ${event.getY(pointerIndex)}"
                    )
                    orchestrator.handleTouch(event.getX(pointerIndex), event.getY(pointerIndex))
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    Log.d(
                        "touch_listener",
                        "pointer_down ${event.getX(pointerIndex)}, ${event.getY(pointerIndex)}"
                    )
                    orchestrator.handleTouch(event.getX(pointerIndex), event.getY(pointerIndex))
                }
            }
        }

        return true
    }

    override fun getScore(): Int = orchestrator.score
}