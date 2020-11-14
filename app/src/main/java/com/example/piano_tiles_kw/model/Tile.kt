package com.example.piano_tiles_kw.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Tile(val width: Float, val height: Float, val cx: Float, private val dropSpeed: Float, private val color: Int) {
    var paint = Paint()
    var cy = 0 - height/2
    init {
        paint.color = color
    }

    /**
     * Draw itself on a canvas
     * @param canvas The canvas where the tile will be drawn
     */
    fun drawTile(canvas: Canvas){
        canvas.drawRect(cx-width/2, cy - height/2, cx+width/2, cy + height/2, paint)
    }

    /**
     * for pre multithread testing
     */
    fun drop(){
        cy += dropSpeed
    }
}