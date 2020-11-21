package com.example.piano_tiles_kw.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.piano_tiles_kw.R
import com.example.piano_tiles_kw.databinding.FragmentDescriptionBinding

class GameplayFragment : Fragment(),
    View.OnClickListener, OnTouchListener {
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return view
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

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return false
    }

    companion object {
        fun newInstance(title: String?): GameplayFragment {
            val fragment = GameplayFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
}