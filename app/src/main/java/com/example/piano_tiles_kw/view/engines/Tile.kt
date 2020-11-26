package com.example.piano_tiles_kw.view.engines

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import com.example.piano_tiles_kw.view.UIThreadWrapper

class Tile(
    val width: Float,
    val height: Float,
    val cx: Float,
    private val dropSpeed: Float,
    private val envHeight: Float,
    color: Int
) {
    var paint = Paint()
    var cy = 0 - height
    var isOut = false

    init {
        paint.color = color
    }

    fun drop() {
        if (!isOut){
            cy += dropSpeed
            if (cy > envHeight){
                isOut = true
            }
        }
    }

    /**
     * A class that draws this particular tile on the canvas
     * Will be passed to the engine to draw in the UI Thread
     */
    inner class Drawer: TileDrawer() {
        override fun drawTile(canvas: Canvas) {
            canvas.drawRect(cx-width/2, cy, cx+width/2, cy + height, paint)
        }
    }
}