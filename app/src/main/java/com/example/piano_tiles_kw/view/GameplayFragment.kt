package com.example.piano_tiles_kw.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.piano_tiles_kw.databinding.FragmentGameplayBinding
import com.example.piano_tiles_kw.model.Page
import com.example.piano_tiles_kw.view.engines.raining.RainingGameEngine
import com.example.piano_tiles_kw.view.engines.GameEngine
import com.example.piano_tiles_kw.viewmodel.MainVM

// Contains the game using canvas

class GameplayFragment : Fragment(), OnEndGameListener{
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentGameplayBinding
    private lateinit var engine : GameEngine
    private lateinit var vm : MainVM

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProvider(requireActivity()).get(MainVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameplayBinding.inflate(inflater, container, false)
        binding.ivCanvas.addOnLayoutChangeListener{ _, _, _, _, _, _, _, _, _ ->
            if (binding.ivCanvas.width > 0 && binding.ivCanvas.height>0){
                engine = RainingGameEngine(requireActivity(), binding.ivCanvas,this)
                engine.startGame()
            }
        }
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


    companion object {
        fun newInstance(title: String?): GameplayFragment {
            val fragment = GameplayFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun OnEndGame() {

        val currHighscore = vm.getHighScore().value
        val score = engine.getScore()
        vm.setScore(score)
        if(score > currHighscore!!) {
            listener.updateHighscore((score))
        }

        listener.changePage(Page.RESULT)
    }
}