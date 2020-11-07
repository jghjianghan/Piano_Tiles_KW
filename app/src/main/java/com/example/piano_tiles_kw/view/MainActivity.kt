package com.example.piano_tiles_kw.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.piano_tiles_kw.R
import com.example.piano_tiles_kw.databinding.ActivityMainBinding
import com.example.piano_tiles_kw.viewmodel.MainVM
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vm: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this).get(MainVM::class.java)
        vm.getContoh().observe(this, {
            binding.tv.text = it
        })

        binding.actionBtn.setOnClickListener { vm.setContoh("clicked") }
        binding.nextBtn.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, ContohFragment.newInstance())
            ft.commit()
        }
    }
}