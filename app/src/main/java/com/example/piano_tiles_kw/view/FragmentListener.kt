package com.example.piano_tiles_kw.view

import com.example.piano_tiles_kw.model.Page

interface FragmentListener {
    fun changePage(page: Page)
    fun closeApplication()
}