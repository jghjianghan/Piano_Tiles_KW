package com.example.piano_tiles_kw.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.piano_tiles_kw.databinding.FragmentGameplayBinding
import com.example.piano_tiles_kw.model.Page
import com.example.piano_tiles_kw.view.engines.RainingGameEngine
import com.example.piano_tiles_kw.view.engines.GameEngine

class GameplayFragment : Fragment(),
    View.OnClickListener{
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentGameplayBinding
    private lateinit var engine : GameEngine
//    private lateinit var handler: UIThreadWrapper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameplayBinding.inflate(inflater, container, false)
        binding.ivCanvas.addOnLayoutChangeListener{ _, _, _, _, _, _, _, _, _ ->
            if (binding.ivCanvas.width > 0 && binding.ivCanvas.height>0){
                engine = RainingGameEngine(requireActivity(), binding.ivCanvas)
                engine.startGame()
            }
        }

        binding.btnTemp.setOnClickListener(this)
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
        if(v == binding.btnTemp) {
            listener.changePage(Page.RESULT)
        }

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