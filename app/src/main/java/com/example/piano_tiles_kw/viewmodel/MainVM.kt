package com.example.piano_tiles_kw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.piano_tiles_kw.model.GameMode
import com.example.piano_tiles_kw.model.SharedPrefWriter

/**
 * Class that manages view model, this class extends class ViewModel. This class contains all live data needed in the game.
 */
class MainVM : ViewModel() {

    // gameMode
    private val gameMode = MutableLiveData<GameMode>()
    fun getGameMode():LiveData<GameMode> = gameMode
    fun setGameMode(newMode : GameMode) {
        gameMode.value = newMode
    }
    // end of gameMode

    // score
    private val score = MutableLiveData<Int>()
    fun getScore():LiveData<Int> = score
    fun setScore(newScore : Int) {
        score.value = newScore
    }
    // end of score

    // highScore
    private val highScore = MutableLiveData<Int>()
    fun getHighScore():LiveData<Int> = highScore
    fun setHighScore(newHighScore : Int) {
        highScore.value = newHighScore
    }
    // end of highScore

    // toolbarTitle
    private val toolbarTitle = MutableLiveData<String>()
    fun getToolbarTitle():LiveData<String> = toolbarTitle
    fun setToolbarTitle(newTitle : String) {
        toolbarTitle.value = newTitle
    }
    // end of toolbarTitle

}