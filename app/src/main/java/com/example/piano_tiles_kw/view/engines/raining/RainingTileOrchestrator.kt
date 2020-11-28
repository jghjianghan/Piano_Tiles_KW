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
    var tileHeight = 1.8f * tileWidth
    private val missed = MissedAnimation(tileHeight, 10, 20)

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
                tiles.add(NormalTile(tileWidth, tileHeight, laneCenters.random(), envHeight, tileColor))
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
                var missedTile = false
                val currDropSpeed = dropSpeed
                while (iter.hasNext()){
                    val tile = iter.next()
                    tile.drop(currDropSpeed)
                    drawers.add(tile.Drawer())
                    if (tile.isOut){
                        if (!missedTile && tile.isClickable){
                            missedTile = true
                            tile.onMissed()
                        }
                        outTiles.add(tile)
                    }
                }

                if (missedTile){
                    stopFlag = true;
                    missedMechanism()
                    return;
                }
                tiles.removeAll(outTiles)
                handler.redrawCanvas(drawers)
                sleep(delay)
            }
        }
    }

    private inner class Speeder(
        private val interval: Long = 2000,
        private val speedUpFactor: Float = 1.1f,
    ): Thread() {
        var stopFlag = false
        override fun run() {
            while (!stopFlag){
                sleep(interval)
                dropSpeed *= speedUpFactor
            }
        }
    }

    private inner class MissedAnimation(
        private val totalDisplacement: Float,
        private val iteration: Int,
        private val delay: Long
    ): Thread(){
        private val step = totalDisplacement/iteration

        override fun run() {
            println("step $step")
            for (i in 1..iteration){
                val drawers = ArrayList<TileDrawer>()
                for (tile in tiles){
                    tile.lift(step)
                    drawers.add(tile.getDrawer())
                }
                println("drawers: " + drawers.size)
                handler.redrawCanvas(drawers)
                sleep(delay)
            }
        }
    }
    private fun missedMechanism(){
        stop()
        println("missedStarting")
        missed.start()
    }

    fun pause(){
        TODO("Yet to be implemented")
    }

    fun resume(){
        TODO("Yet to be implemented")
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
        if (!engine.isOver){
            engine.stopGame()
        }
    }
}