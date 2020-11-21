package com.example.piano_tiles_kw.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.piano_tiles_kw.databinding.FragmentDescriptionBinding
import com.example.piano_tiles_kw.databinding.FragmentResultBinding

class ResultFragment : Fragment(),
    View.OnClickListener, OnTouchListener {
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.btnAgain.setOnClickListener(this)
        binding.btnMenu.setOnClickListener(this)

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
        if (binding.btnAgain == v) {
            listener.changePage(2)
        } else if (binding.btnMenu == v) {
            listener.changePage(1)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return false
    }

    companion object {
        fun newInstance(title: String?): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
}