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
 * Generic Class that manages a piano tile game in the given ImageView
 * @param context The context of the running game
 * @param iv The ImageView where the game will be displayed and played
 */
abstract class GameEngine (private val context: Context, private val iv: ImageView) {
    private val mBitmap: Bitmap = Bitmap.createBitmap(
        iv.width,
        iv.height,
        Bitmap.Config.ARGB_8888
    )
    protected val mCanvas = Canvas(mBitmap)
    protected val strokePaint = Paint()
    protected val fillPaint = Paint()

    init {
        iv.setImageBitmap(mBitmap)
    }

    /**
     * initiate the colors and styles of the
     */
    open fun initiatePaints(){
        strokePaint.color = Color.BLACK
        strokePaint.style = Paint.Style.STROKE
        fillPaint.color = Color.BLACK
    }

    /**
     * Resets the canvas' background and redraw its lane separator
     */
    protected abstract fun clearCanvas()

    /**
     * Resets the canvas and then redraw all the elements (tiles)
     */
    protected abstract fun redraw()

    protected abstract fun startGame()
}