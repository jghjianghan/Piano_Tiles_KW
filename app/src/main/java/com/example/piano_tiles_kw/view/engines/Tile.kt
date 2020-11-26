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
    var cy = 0 - height/2
    var isOut = false

    init {
        paint.color = color
    }

    fun drop() {
        if (!isOut){
            cy += dropSpeed
            println("envHeight: $envHeight")
            println("cy: ${cy - height/2}")
            if (cy - height/2 >= envHeight){
                isOut = true
            }
        }
    }

    /**
     * Draw itself on a canvas
     * @param canvas The canvas where the tile will be drawn
     */
    inner class Drawer: TileDrawer() {
        override fun drawTile(canvas: Canvas) {
            canvas.drawRect(cx-width/2, cy - height/2, cx+width/2, cy + height/2, paint)
        }
    }
}