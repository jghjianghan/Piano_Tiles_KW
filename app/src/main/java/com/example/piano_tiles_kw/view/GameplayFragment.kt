package com.example.piano_tiles_kw.view

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.piano_tiles_kw.databinding.FragmentGameplayBinding
import com.example.piano_tiles_kw.model.GameMode
import com.example.piano_tiles_kw.model.Page
import com.example.piano_tiles_kw.view.engines.GameEngine
import com.example.piano_tiles_kw.view.engines.arcade.ArcadeGameEngine
import com.example.piano_tiles_kw.view.engines.classic.ClassicGameEngine
import com.example.piano_tiles_kw.view.engines.raining.RainingGameEngine
import com.example.piano_tiles_kw.view.engines.tilt.TiltGameEngine
import com.example.piano_tiles_kw.viewmodel.MainVM

// Contains the game using canvas

class GameplayFragment : SensorEventListener, Fragment(), GameEngine.GameListener{
    private lateinit var listener: FragmentListener
    private lateinit var binding : FragmentGameplayBinding
    private lateinit var engine : GameEngine
    private lateinit var vm : MainVM
    private lateinit var gameMode: GameMode
    private lateinit var sensorManager: SensorManager
    private  var accelerometer: Sensor? = null
    private var sensorData: SensorData = SensorData()


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
                        engine = ClassicGameEngine(requireActivity(), binding.ivCanvas, this)
                    }
                    GameMode.ARCADE -> {
                        engine = ArcadeGameEngine(requireActivity(), binding.ivCanvas, this)
                    }
                    GameMode.RAINING -> {
                        engine = RainingGameEngine(requireActivity(), binding.ivCanvas, this)
                    }
                    GameMode.TILT -> {
                        engine = TiltGameEngine(requireActivity(), binding.ivCanvas, this, sensorData)
                    }
                }
                engine.startGame()

            }
        }

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        return binding.root
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var sensorType = event!!.sensor.getType()
        if(sensorType == Sensor.TYPE_ACCELEROMETER){
            sensorData.sensorX = event.values[0]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        if(accelerometer != null){
            sensorManager.registerListener(
                this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
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
                if (score > currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
            GameMode.CLASSIC -> {
                val currHighscore = vm.getClassicHighScore().value
                val score = engine.getScore() as Float
                vm.setClassicScore(score)
                if (score < currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
            GameMode.ARCADE -> {
                val currHighscore = vm.getArcadeHighScore().value
                val score = engine.getScore() as Int
                vm.setArcadeScore(score)
                if (score > currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
            GameMode.TILT -> {
                val currHighscore = vm.getTiltHighScore().value
                val score = engine.getScore() as Int
                vm.setTiltScore(score)
                if (score > currHighscore!!) {
                    listener.updateHighscore(score, gameMode)
                }
            }
        }

        listener.changePage(Page.RESULT)
    }
}