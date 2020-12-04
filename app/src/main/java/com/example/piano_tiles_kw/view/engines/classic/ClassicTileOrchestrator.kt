package com.example.piano_tiles_kw.view.engines.classic

import android.graphics.Color
import com.example.piano_tiles_kw.view.UIThreadWrapper
import com.example.piano_tiles_kw.view.engines.CircularFalseMark
import com.example.piano_tiles_kw.view.engines.NormalTile
import com.example.piano_tiles_kw.view.engines.TileDrawer
import com.example.piano_tiles_kw.view.engines.TileOrchestrator
import java.util.*
import kotlin.collections.ArrayList

class ClassicTileOrchestrator(
    private val engine: ClassicGameEngine,
    private val handler: UIThreadWrapper,
    laneCenters : ArrayList<Float>,
    private val tileWidth: Float,
    private val envHeight: Float,
    private val totalLine : Int,
    private val linePerScreen : Int

): TileOrchestrator(laneCenters, 0f) {
    private val tiles = Vector<NormalTile>()
    private val tileHeight : Float = envHeight / linePerScreen
    private val dropper = Dropper(10,15)
    private val spawner = Spawner()
    private val laneHeightCenters = ArrayList<Float>() // index 0 di atas luar layar

    init {
        var currHeight = -tileHeight
        for(i in 0..linePerScreen) {
            laneHeightCenters.add(currHeight)
            currHeight += tileHeight
        }
    }

    fun start() {
        println("orchestrator starting")
        spawner.start()
        dropper.start()
    }

    private inner class Spawner {
        private var normalTilesSpawned = 0 // Black Tiles

        fun start() {

            // spawn yelow starting tiles
            for (i in 0..3) {
                tiles.add(NormalTile(tileWidth,tileHeight,laneCenters[i],laneHeightCenters[linePerScreen] ,envHeight, Color.YELLOW,false))
            }

            // spawn initial black tiles
            for (i in linePerScreen-1 downTo 1) {
                val cx = laneCenters.random()
                tiles.add(NormalTile(tileWidth,tileHeight,cx,laneHeightCenters[i] ,envHeight, Color.BLACK))
                normalTilesSpawned++
            }

            val drawers = ArrayList<TileDrawer>()
            for (tile in tiles){
                drawers.add(tile.getDrawer())
            }
            handler.redrawCanvas(drawers)
        }

        fun spawn() {
            if(normalTilesSpawned < totalLine) {
                val cx = laneCenters.random()
                tiles.add(NormalTile(tileWidth,tileHeight,cx,laneHeightCenters[0] ,envHeight, Color.BLACK))
                normalTilesSpawned++
            }else {
                for (i in 0..3) {
                    tiles.add(NormalTile(tileWidth,tileHeight,laneCenters[i],laneHeightCenters[0] ,envHeight, Color.GREEN, false))
                }
            }
        }
    }

    private inner class Dropper(
        private val iterationPerTile: Int,
        private val delay: Long
    ) : Thread() {
        var stopFlag = false
        var pauseFlag = false

        private var totalIteration = 0
        private var iteration = 0
        private val step = tileHeight/iterationPerTile

        override fun run() {
            while(!stopFlag) {
                if(!pauseFlag) {
                    if(iteration < totalIteration) {
                        val drawers = ArrayList<TileDrawer>()
                        if(tiles[tiles.size - 1].cy >= laneHeightCenters[1]) {
                            spawner.spawn()
                        }
                        for (tile in tiles){
                            tile.drop(step)
                            drawers.add(tile.getDrawer())
                        }
                        handler.redrawCanvas(drawers)
                        iteration++
                    }else {
                        // reset to 0
                        totalIteration = 0
                        iteration = 0
                    }
                }
                sleep(delay)

            }
        }

        fun drop() {
            totalIteration += iterationPerTile
        }

    }


    fun pause(){
        TODO("Yet to be implemented")
    }

    fun resume(){
        TODO("Yet to be implemented")
    }

    override fun handleTouch(x: Float, y: Float) {
        if (!dropper.stopFlag && !dropper.pauseFlag){
            val iter = tiles.iterator()
            while (iter.hasNext()){
                val tile = iter.next()
                if(tile.isClickable) {
                    if (tile.isTileTouched(x, y)){
                        tile.onClick()
                        dropper.drop()
                        score++
                    }
                    else if(y >= tile.cy && y <= tile.cy + tileHeight ){ // miss
                        touchMissMechanism(x, tile.cy)
                    }
                    return
                }
            }

        }
    }

    private fun touchMissMechanism(x: Float, yc: Float) {
        internalStop()
        val drawers = ArrayList<TileDrawer>()

        var xc = 0f
        for(xcenter in laneCenters) {
            if(x >= xcenter - tileWidth/2 && x <= xcenter + tileWidth/2) {
                xc = xcenter
            }
        }

        tiles.add(NormalTile(tileWidth,tileHeight,xc,yc,envHeight,Color.RED,false))

        val iter2 = tiles.iterator()
        while (iter2.hasNext()){
            val tile = iter2.next()
            drawers.add(tile.Drawer())
        }

        handler.redrawCanvas(drawers)
        Thread {
            Thread.sleep(1000)
            stop()
        }.start()
    }

    private fun internalStop(){
        dropper.stopFlag = true
    }

    fun stop(){
        internalStop()
        println("stopping orchestrator")
        if (!engine.isOver){
            handler.stopGame()
        }
    }
}