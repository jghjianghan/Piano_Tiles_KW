package com.example.piano_tiles_kw.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Class that manages read and write in Shared Preference memory system
 * @param context The context of the running game
 */
class SharedPrefWriter(context: Context) {
    var sharedPref: SharedPreferences
    companion object {
        const val NAMA_SHARED_PREF = "piano_tiles_kw_pref"
        const val KEY_HIGHSCORE = "HIGHSCORE"
    }

    init {
        this.sharedPref = context.getSharedPreferences(NAMA_SHARED_PREF, 0)
    }

    fun saveHighscore(newHighscore:Int){
        val editor = sharedPref.edit()
        editor.putInt(KEY_HIGHSCORE, newHighscore)
        editor.commit()
    }
    fun getHighscore(): Int{
        return sharedPref.getInt(KEY_HIGHSCORE, 0)
    }
}