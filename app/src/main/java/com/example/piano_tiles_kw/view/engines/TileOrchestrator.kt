package com.example.piano_tiles_kw.view.engines

import com.example.piano_tiles_kw.model.audio.PianoPlayer

abstract class TileOrchestrator(
    protected val laneCenters : ArrayList<Float>,
    protected var dropSpeed: Float,
    protected val pianoPlayer: PianoPlayer? = null
) {
    var score = 0f

    abstract fun handleTouch(x: Float, y: Float)
}