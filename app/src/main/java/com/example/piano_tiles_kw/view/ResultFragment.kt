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
import com.example.piano_tiles_kw.model.Page

class ResultFragment : Fragment(),
    View.OnClickListener{
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
            listener.changePage(Page.GAMEPLAY)
        } else if (binding.btnMenu == v) {
            listener.changePage(Page.MENU)
        }
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