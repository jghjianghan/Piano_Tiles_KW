package com.example.piano_tiles_kw.view

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.piano_tiles_kw.view.engines.GameEngine
import com.example.piano_tiles_kw.view.engines.TileDrawer
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class UIThreadWrapper(
    private val engine: GameEngine,
    looper: Looper
): Handler(looper) {
    companion object {
        private const val REDRAW_CANVAS = 1
        private const val STOP_GAME = 2
    }

    override fun handleMessage(msg: Message) {
        when(msg.what){
            REDRAW_CANVAS -> engine.redraw(msg.obj as ArrayList<TileDrawer>)
            STOP_GAME -> engine.stopGame()
        }
    }
    fun redrawCanvas(drawers: ArrayList<TileDrawer>){
        val msg = Message()
        msg.what = REDRAW_CANVAS
        msg.obj = drawers
        this.sendMessage(msg)
    }
    fun stopGame(){
        val msg = Message()
        msg.what = STOP_GAME
        this.sendMessage(msg)
    }
}
