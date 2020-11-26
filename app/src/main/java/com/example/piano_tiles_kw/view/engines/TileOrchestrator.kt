package com.example.piano_tiles_kw.view.engines

import android.graphics.Color
import com.example.piano_tiles_kw.view.UIThreadWrapper
import java.util.*
import kotlin.collections.ArrayList

class TileOrchestrator(
    private val handler: UIThreadWrapper,
    private val laneCenters : ArrayList<Float>,
    var tileWidth: Float,
    spawnDelay: Long,
    private var initSpeed: Float,
    var envHeight: Float,
    private val tileColor: Int
) {
    private val tiles = Vector<Tile>()
    private val spawner = Spawner(spawnDelay, tileWidth, initSpeed, envHeight, tileColor)
    private val puller = Puller()

    fun start() {
        println("orchestrator starting")
        spawner.start()
        tiles.add(Tile(tileWidth, tileWidth*1.6f, laneCenters.random(), initSpeed, envHeight, tileColor))
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
            Thread.sleep(delay)
            while (!stopFlag){
                tiles.add(Tile(tileWidth, tileWidth*1.6f, laneCenters.random(), initSpeed, envHeight, tileColor))
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
                for (tile in tiles){
                    println("dropping")
                    tile.drop()
                    drawers.add(tile.Drawer())
                }
                handler.redrawCanvas(drawers)
                Thread.sleep(delay)
            }
        }
    }

    fun pause(){

    }

    fun resume(){

    }

    fun stop(){
        spawner.stopFlag = true
        puller.stopFlag = true
        println("stopping orchestrator")
    }
}