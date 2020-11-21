package com.example.piano_tiles_kw.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.piano_tiles_kw.R
import com.example.piano_tiles_kw.databinding.ActivityMainBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager = this.supportFragmentManager
        menuFragment = MenuFragment()
        descriptionFragment = DescriptionFragment()
        gameplayFragment = GameplayFragment()
        resultFragment = ResultFragment()

        vm = ViewModelProvider(this).get(MainVM::class.java)
        changePage(4)
    }

    override fun changePage(page: Int) {
        val ft = fragmentManager.beginTransaction()
        if (page == 1) {
            if (menuFragment.isAdded) {
                ft.show(menuFragment)
            } else {
                ft.add(R.id.fragment_container, menuFragment)
            }
            if (descriptionFragment.isAdded) {
                ft.hide(descriptionFragment)
            }
            if (resultFragment.isAdded) {
                ft.hide(resultFragment)
            }
        } else if (page == 2) {
            if (descriptionFragment.isAdded) {
                ft.show(descriptionFragment)
            } else {
                ft.add(R.id.fragment_container, descriptionFragment)
            }
            if (menuFragment.isAdded) {
                ft.hide(menuFragment)
            }
            if (resultFragment.isAdded) {
                ft.hide(resultFragment)
            }
        } else if (page == 3) {
            if (gameplayFragment.isAdded) {
                ft.show(gameplayFragment)
            } else {
                ft.add(R.id.fragment_container, gameplayFragment)
            }
            if (descriptionFragment.isAdded) {
                ft.hide(descriptionFragment)
            }
        } else if (page == 4) {
            if (resultFragment.isAdded) {
                ft.show(resultFragment)
            } else {
                ft.add(R.id.fragment_container, resultFragment)
            }
            if (gameplayFragment.isAdded) {
                ft.hide(gameplayFragment)
            }
        }
        ft.commit()
    }

    override fun closeApplication() {
        moveTaskToBack(true)
        finish()
    }
}