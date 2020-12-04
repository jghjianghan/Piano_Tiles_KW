package com.example.piano_tiles_kw.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.piano_tiles_kw.databinding.FragmentGameplayBinding
import com.example.piano_tiles_kw.model.GameMode
import com.example.piano_tiles_kw.model.Page
import com.example.piano_tiles_kw.view.engines.raining.RainingGameEngine
import com.example.piano_tiles_kw.view.engines.GameEngine
import com.example.piano_tiles_kw.view.engines.classic.ClassicGameEngine
import com.example.piano_tiles_kw.viewmodel.MainVM

// Contains the game using canvas

class GameplayFragment : Fragment(), GameEngine.GameListener{
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentGameplayBinding
    private lateinit var engine : GameEngine
    private lateinit var vm : MainVM
    private lateinit var gameMode: GameMode

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProvider(requireActivity()).get(MainVM::class.java)
        this.gameMode = vm.getGameMode().value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameplayBinding.inflate(inflater, container, false)
        binding.ivCanvas.addOnLayoutChangeListener{ _, _, _, _, _, _, _, _, _ ->
            if (binding.ivCanvas.width > 0 && binding.ivCanvas.height>0){
                when(vm.getGameMode().value) {
                    GameMode.CLASSIC -> {
                        engine = ClassicGameEngine(requireActivity(), binding.ivCanvas,this)
                        engine.startGame()
                    }
                    else -> {
                        engine = RainingGameEngine(requireActivity(), binding.ivCanvas,this)
                        engine.startGame()
                    }
                }

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

    override fun onScoreChanged(score: Number) {
        binding.tvScoreValue.text = score.toString()
    }
    override fun onEndGame() {
        Log.d("raining end game", "-")
        when(this.gameMode){
            GameMode.RAINING -> {
                val currHighscore = vm.getRainingHighScore().value
                Log.d("raining gp highscore", currHighscore.toString())
                val score = engine.getScore() as Int
                Log.d("raining gp score", score.toString())
                vm.setRainingScore(score)
                if(score > currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
            GameMode.CLASSIC -> {
                val currHighscore = vm.getClassicHighScore().value
                val score = engine.getScore() as Float
                vm.setClassicScore(score)
                if(score < currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
            GameMode.ARCADE -> {
                val currHighscore = vm.getArcadeHighScore().value
                val score = engine.getScore() as Int
                vm.setArcadeScore(score)
                if(score < currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
            GameMode.TILT -> {
                val currHighscore = vm.getTiltHighScore().value
                val score = engine.getScore() as Int
                vm.setTiltScore(score)
                if(score < currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
        }

        listener.changePage(Page.RESULT)
    }
}