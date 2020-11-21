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
import com.example.piano_tiles_kw.databinding.FragmentDescriptionBinding
import com.example.piano_tiles_kw.model.Page

class DescriptionFragment : Fragment(),
    View.OnClickListener{
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentDescriptionBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false)

        binding.btnStart.setOnClickListener(this)

        binding.btnCancel.setOnClickListener(this)
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
        if (binding.btnStart == v) {
            listener.changePage(Page.GAMEPLAY)
        } else if (binding.btnCancel == v) {
            listener.changePage(Page.MENU)
        }
    }

    companion object {
        fun newInstance(title: String?): DescriptionFragment {
            val fragment = DescriptionFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
}