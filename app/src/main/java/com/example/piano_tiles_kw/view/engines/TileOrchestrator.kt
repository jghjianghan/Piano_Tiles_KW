package com.example.piano_tiles_kw.view.engines

abstract class TileOrchestrator(
    protected val laneCenters : ArrayList<Float>,
    protected var dropSpeed: Float
) {
    var score = 0

    abstract fun handleTouch(x: Float, y: Float)
}