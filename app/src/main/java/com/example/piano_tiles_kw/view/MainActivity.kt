package com.example.piano_tiles_kw.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.piano_tiles_kw.R
import com.example.piano_tiles_kw.databinding.ActivityMainBinding
import com.example.piano_tiles_kw.model.GameMode
import com.example.piano_tiles_kw.model.Page
import com.example.piano_tiles_kw.model.SharedPrefWriter
import com.example.piano_tiles_kw.view.MenuFragment.Companion.newInstance
import com.example.piano_tiles_kw.viewmodel.MainVM

class MainActivity : AppCompatActivity(), FragmentListener {
    private lateinit var vm: MainVM
    private lateinit var menuFragment: MenuFragment
    private lateinit var descriptionFragment: DescriptionFragment
    private lateinit var gameplayFragment: GameplayFragment
    private lateinit var resultFragment: ResultFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefWriter: SharedPrefWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager = this.supportFragmentManager
        menuFragment = MenuFragment()
        descriptionFragment = DescriptionFragment()
        gameplayFragment = GameplayFragment()
        resultFragment = ResultFragment()
        sharedPrefWriter = SharedPrefWriter(this)

        vm = ViewModelProvider(this).get(MainVM::class.java)
        initHighscore()

        changePage(Page.MENU)
    }

    override fun changePage(p : Page) {
        val ft = fragmentManager.beginTransaction()
        when(p) {
            Page.MENU -> {
                ft.replace(R.id.fragment_container, menuFragment)
            }

            Page.DESCRIPTION -> {
                ft.replace(R.id.fragment_container, descriptionFragment).addToBackStack(null)
            }

            Page.GAMEPLAY -> {
                ft.replace(R.id.fragment_container, gameplayFragment)
            }

            Page.RESULT -> {
                ft.replace(R.id.fragment_container, resultFragment)
            }
        }
        ft.commit()
    }


    override fun closeApplication() {
        moveTaskToBack(true)
        finish()
    }

    private fun initHighscore (){
        vm.setClassicHighScore(sharedPrefWriter.getClassicHighscore())
        vm.setArcadeHighScore(sharedPrefWriter.getArcadeHighscore())
        vm.setRainingHighScore(sharedPrefWriter.getRainingHighscore())
        vm.setTiltHighScore(sharedPrefWriter.getTiltHighscore())
    }

    override fun updateHighscore(newHighscore : Number, mode: GameMode) {
        when(mode){
            GameMode.RAINING -> {
                val hs = newHighscore as Int
                sharedPrefWriter.saveRainingHighscore(hs)
                vm.setRainingScore(hs)
            }
            GameMode.CLASSIC -> {
                val hs = newHighscore as Float
                sharedPrefWriter.saveClassicHighscore(hs)
                vm.setClassicScore(hs)
            }
            GameMode.ARCADE -> {
                val hs = newHighscore as Int
                sharedPrefWriter.saveArcadeHighscore(hs)
                vm.setArcadeScore(hs)
            }
            GameMode.TILT -> {
                val hs = newHighscore as Int
                sharedPrefWriter.saveTiltHighscore(hs)
                vm.setTiltScore(hs)
            }
        }
    }
}