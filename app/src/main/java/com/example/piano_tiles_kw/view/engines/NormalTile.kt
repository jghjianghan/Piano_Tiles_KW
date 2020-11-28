package com.example.piano_tiles_kw.view.engines

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.piano_tiles_kw.view.UIThreadWrapper

/**
 * The normal behaving tile
 * Drops normally, disappear on click
 * The anchor point is at the center-top of the tile
 */
class NormalTile(
    width: Float,
    height: Float,
    cx: Float,
    private val envHeight: Float,
    color: Int
) : Tile(width, height, cx) {
    var paint = Paint()
    var cy = 0 - height
    var isOut = false
    var isClickable = true
    var dimColor = Color.GRAY
    var missedColor = Color.RED

    init {
        paint.color = color
    }

    override fun drop(dy: Float): Boolean {
        return if (!isOut) {
            cy += dy
            if (cy > envHeight) {
                isOut = true
            }
            true
        } else {
            false
        }
    }

    override fun lift(dy: Float) {
        cy -= dy
    }

    override fun isTileTouched(x: Float, y: Float): Boolean {
        return isClickable && x >= cx - width / 2 && x <= cx + width / 2 && y >= cy && y <= cy + height
    }

    override fun onClick() {
        isClickable = false
        paint.color = dimColor
    }

    override fun onMissed() {
        isClickable = false
        paint.color = missedColor
    }

    override fun getDrawer(): TileDrawer = Drawer()

    /**
     * A class that draws this particular tile on the canvas
     * Will be passed to the engine to draw in the UI Thread
     */
    inner class Drawer : TileDrawer() {
        override fun drawTile(canvas: Canvas) {
            canvas.drawRect(cx - width / 2, cy, cx + width / 2, cy + height, paint)
        }
    }
}