package com.example.piano_tiles_kw.view.engines

abstract class TileOrchestrator(
    protected val laneCenters : ArrayList<Float>,
    protected var dropSpeed: Float
) {
    var score = 0

    /**
     * @param x the x coordinate of the touch location
     * @param y the x coordinate of the touch location
     * @return true if the touch was a successful touch
     */
    abstract fun handleTouch(x: Float, y: Float): Boolean
}