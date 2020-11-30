package com.example.piano_tiles_kw.view.engines.raining

import android.util.Log
import com.example.piano_tiles_kw.view.UIThreadWrapper
import com.example.piano_tiles_kw.view.engines.CircularFalseMark
import com.example.piano_tiles_kw.view.engines.NormalTile
import com.example.piano_tiles_kw.view.engines.TileDrawer
import com.example.piano_tiles_kw.view.engines.TileOrchestrator
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

class RainingTileOrchestrator(
    private val engine: RainingGameEngine,
    private val handler: UIThreadWrapper,
    laneCenters : ArrayList<Float>,
    tileWidth: Float,
    spawnDelay: Long,
    initSpeed: Float,
    envHeight: Float,
    tileColor: Int
): TileOrchestrator(laneCenters, initSpeed) {
    private val tiles = Vector<RainingTile>()
    private val spawner = Spawner(spawnDelay, tileWidth, 4f, envHeight, tileColor)
    private val puller = Puller()
    private val speeder = Speeder()
    var tileHeight = 1.8f * tileWidth
    private val missed = MissedAnimation(tileHeight, 10, 20)

    fun start() {
        println("orchestrator starting")
        spawner.start()
        puller.start()
        speeder.start()
    }

    private inner class Spawner(
        private var delay: Long,
        private val tileWidth: Float,
        private val speedDeviationFactor: Float,
        private val envHeight: Float,
        private val tileColor: Int
    ): Thread() {
        var stopFlag = false
        private val speedUpFactor: Float = .95f
        private val speedUpIteration = 15
        private var speedUpCounter = 0
        override fun run() {
            while (!stopFlag){
                val tileSpeed = (ThreadLocalRandom.current().nextFloat() - 0.5f) * speedDeviationFactor + dropSpeed
                Log.d("dropSpeed", dropSpeed.toString())
                Log.d("Tilespeed", tileSpeed.toString())
                tiles.add(RainingTile(tileWidth, tileHeight, laneCenters.random(), envHeight, tileSpeed, tileColor))
                sleep(delay)
                if (speedUpCounter == speedUpIteration){
                    speedUpCounter = 0
                    delay = (delay*speedUpFactor).toLong()
                }
            }
        }
    }
    private inner class Puller: Thread() {
        private var delay: Long = 20
        var stopFlag = false
        override fun run() {
            while (!stopFlag){
                val drawers = ArrayList<TileDrawer>()
                val outTiles = ArrayList<RainingTile>()
                val iter = tiles.iterator()
                var missedTile = false
                while (iter.hasNext()){
                    val tile = iter.next()
                    tile.drop()
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
                    stopFlag = true
                    missedMechanism()
                    return
                }
                tiles.removeAll(outTiles)
                handler.redrawCanvas(drawers)
                sleep(delay)
            }
        }
    }

    private inner class Speeder(
        private val interval: Long = 15000,
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
        totalDisplacement: Float,
        private val iteration: Int,
        private val delay: Long,
        private val postExecDelay: Long = 1000
    ) {
        private val step = totalDisplacement/iteration

        fun start() {
            println("step $step")
            for (i in 1..iteration){
                val drawers = ArrayList<TileDrawer>()
                for (tile in tiles){
                    tile.lift(step)
                    drawers.add(tile.getDrawer())
                }
                println("drawers: " + drawers.size)
                handler.redrawCanvas(drawers)
                Thread.sleep(delay)
            }
            Thread.sleep(postExecDelay)
        }
    }
    private fun missedMechanism(){
        println("missedStarting")
        internalStop()
        missed.start()
        stop()
    }

    fun pause(){
        TODO("Yet to be implemented")
    }

    fun resume(){
        TODO("Yet to be implemented")
    }

    override fun handleTouch(x: Float, y: Float) {
        if (!puller.stopFlag){
            val iter = tiles.iterator()
            while (iter.hasNext()){
                val tile = iter.next()
                if (tile.isTileTouched(x, y)){
                    tile.onClick()
                    score++
                    return
                }
            }

            //miss
            touchMissMechanism(x, y)
        }
    }

    private fun touchMissMechanism(x: Float, y: Float) {
        internalStop()
        val drawers = ArrayList<TileDrawer>()
        val iter2 = tiles.iterator()
        while (iter2.hasNext()){
            val tile = iter2.next()
            drawers.add(tile.Drawer())
        }
        drawers.add(CircularFalseMark(x, y, 40f).Drawer())
        handler.redrawCanvas(drawers)
        Thread {
            Thread.sleep(1000)
            stop()
        }.start()
    }

    private fun internalStop(){
        spawner.stopFlag = true
        puller.stopFlag = true
        speeder.stopFlag = true
    }
    fun stop(){
        internalStop()
        println("stopping orchestrator")
        if (!engine.isOver){
            handler.stopGame()
        }
    }
}