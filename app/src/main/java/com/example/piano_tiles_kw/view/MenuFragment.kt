package com.example.piano_tiles_kw.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.piano_tiles_kw.R
import com.example.piano_tiles_kw.databinding.FragmentMenuBinding
import com.example.piano_tiles_kw.model.Page

// Contains the main menu page to select game modes

class MenuFragment : Fragment(),
    View.OnClickListener {
    private lateinit var listener: FragmentListener
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuBinding.inflate(
            inflater,
            container,
            false
        )

        binding.btnClassic.setOnClickListener(this)
        binding.btnArcade.setOnClickListener(this)
        binding.btnTilt.setOnClickListener(this)
        binding.btnExit.setOnClickListener(this)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            listener = context
        } else {
            throw ClassCastException(
                context.toString() +
                        "must implement FragmentListener"
            )
        }
    }

    override fun onClick(v: View) {
        if (binding.btnClassic == v) {
            listener.changePage(Page.DESCRIPTION)
        } else if (binding.btnArcade == v) {
            listener.changePage(Page.DESCRIPTION)
        } else if (binding.btnTilt == v) {
            listener.changePage(Page.DESCRIPTION)
        } else if (binding.btnExit == v) {
            listener.closeApplication()
        }
    }
    companion object {
        fun newInstance(title: String?): MenuFragment {
            val fragment = MenuFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
}