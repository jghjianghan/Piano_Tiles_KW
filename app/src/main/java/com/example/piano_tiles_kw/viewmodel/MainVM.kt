package com.example.piano_tiles_kw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.piano_tiles_kw.model.Page

/**
 * Class that manages view model, this class extends class ViewModel. This class contains all live data needed in the game.
 */
class MainVM: ViewModel() {
    // gameMode
    private val gameMode = MutableLiveData<String>()
    fun getGameMode():LiveData<String> = gameMode
    fun setGameMode(newMode : String) {
        gameMode.value = newMode
    }
    // end of gameMode

    // page
    private val page = MutableLiveData<Page>()
    fun getPage():LiveData<Page> = page
    fun setPage(newPage : Page) {
        page.value = newPage
    }
    // end of page

    // highScore
    private val highScore = MutableLiveData<Int>()
    fun getHighScore():LiveData<Int> = highScore
    fun setHighScore(newHighScore : Int) {
        highScore.value = newHighScore
        // TODO -> save ke memory ?
    }
    // end of highScore

    // toolbarTitle
    private val toolbarTitle = MutableLiveData<String>()
    fun getToolbarTitle():LiveData<String> = toolbarTitle
    fun setToolbarTitle(newTitle : String) {
        toolbarTitle.value = newTitle
    }
    // end of toolbarTitle

    init {
        page.value = Page.HOME
        highScore.value = 0
        // TODO -> set highScore dari memory ?
    }

}