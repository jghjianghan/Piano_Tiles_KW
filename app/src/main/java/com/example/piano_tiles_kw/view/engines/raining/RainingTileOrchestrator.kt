package com.example.piano_tiles_kw.view.engines.raining

import android.graphics.Color
import android.util.Log
import com.example.piano_tiles_kw.view.UIThreadWrapper
import com.example.piano_tiles_kw.view.engines.NormalTile
import com.example.piano_tiles_kw.view.engines.TileDrawer
import com.example.piano_tiles_kw.view.engines.TileOrchestrator
import java.util.*
import kotlin.collections.ArrayList

class RainingTileOrchestrator(
    private val engine: RainingGameEngine,
    private val handler: UIThreadWrapper,
    laneCenters : ArrayList<Float>,
    var tileWidth: Float,
    spawnDelay: Long,
    initSpeed: Float,
    var envHeight: Float,
    private val tileColor: Int
): TileOrchestrator(laneCenters, initSpeed) {
    private val tiles = Vector<NormalTile>()
    private val spawner = Spawner(spawnDelay, tileWidth, initSpeed, envHeight, tileColor)
    private val puller = Puller()


    fun start() {
        println("orchestrator starting")
        spawner.start()
        puller.start()
    }

    private inner class Spawner(
        private var delay: Long,
        private val tileWidth: Float,
        private val initSpeed: Float,
        private val envHeight: Float,
        private val tileColor: Int
    ): Thread() {
        var stopFlag = false
        override fun run() {
            while (!stopFlag){
                tiles.add(NormalTile(tileWidth, tileWidth*1.8f, laneCenters.random(), envHeight, tileColor))
                Thread.sleep(delay)
            }
        }
    }
    private inner class Puller: Thread() {
        private var delay: Long = 20
        var stopFlag = false
        override fun run() {
            while (!stopFlag){
                val drawers = ArrayList<TileDrawer>()
                val outTiles = ArrayList<NormalTile>()
                val iter = tiles.iterator()
                while (iter.hasNext()){
                    val tile = iter.next()
                    tile.drop(dropSpeed)
                    drawers.add(tile.Drawer())
                    if (tile.isOut){
                        outTiles.add(tile)
                    }
                }

                tiles.removeAll(outTiles)
                handler.redrawCanvas(drawers)
                sleep(delay)
            }
        }
    }

    fun pause(){

    }

    fun resume(){

    }

    override fun handleTouch(x: Float, y: Float) {
        var successfulClick = false
        val iter = tiles.iterator()
        while (iter.hasNext()){
            val tile = iter.next()
            if (tile.isTileTouched(x, y)){
                successfulClick = true
                tile.onClick()
                score++
                return
            }
        }

        //miss
        Log.d("handle touch", "missed")

        val drawers = ArrayList<TileDrawer>()
        val iter2 = tiles.iterator()
        while (iter2.hasNext()){
            val tile = iter2.next()
            drawers.add(tile.Drawer())
        }
        drawers.add(CircularFalseMark(x, y, 40f).Drawer())
        handler.redrawCanvas(drawers)
        stop()
    }

    fun stop(){
        spawner.stopFlag = true
        puller.stopFlag = true
        println("stopping orchestrator")
        engine.stopGame()
    }
}