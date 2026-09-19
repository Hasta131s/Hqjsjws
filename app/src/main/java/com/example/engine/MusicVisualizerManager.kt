package com.example.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Manages rhythmic music-reactive dynamics for the liquid-glass wallpaper.
 * When enabled, the fluid caustics and floating droplets pulse and expand
 * according to simulated/active rhythmic audio frequencies.
 */
object MusicVisualizerManager {

    private val _isMusicReactive = MutableStateFlow(false)
    val isMusicReactive: StateFlow<Boolean> = _isMusicReactive.asStateFlow()

    // Normalized audio pulse intensity: 1.0f base, peaks up to 2.2f
    private val _rhythmPulse = MutableStateFlow(1.0f)
    val rhythmPulse: StateFlow<Float> = _rhythmPulse.asStateFlow()

    // 4-band audio spectrum levels [bass, mid-low, mid-high, treble]
    private val _spectrumBands = MutableStateFlow(floatArrayOf(0.4f, 0.6f, 0.5f, 0.3f))
    val spectrumBands: StateFlow<FloatArray> = _spectrumBands.asStateFlow()

    private var animationJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun toggleMusicReactive(): Boolean {
        val newState = !_isMusicReactive.value
        _isMusicReactive.value = newState
        if (newState) {
            startAudioSimulation()
        } else {
            stopAudioSimulation()
        }
        return newState
    }

    fun setMusicReactive(enabled: Boolean) {
        _isMusicReactive.value = enabled
        if (enabled) {
            startAudioSimulation()
        } else {
            stopAudioSimulation()
        }
    }

    private fun startAudioSimulation() {
        animationJob?.cancel()
        animationJob = scope.launch {
            var step = 0.0
            while (isActive) {
                step += 0.12
                val beat1 = (sin(step * 1.5) + 1.0) / 2.0 // Bass kick ~120 BPM
                val beat2 = (sin(step * 3.2) + 1.0) / 2.0 // Hi-hat / snare rhythm
                val pulse = (1.0 + (beat1 * 0.75 + beat2 * 0.35)).toFloat()

                _rhythmPulse.value = pulse
                _spectrumBands.value = floatArrayOf(
                    (0.3f + beat1.toFloat() * 0.7f).coerceIn(0.1f, 1.0f),
                    (0.4f + beat2.toFloat() * 0.5f).coerceIn(0.1f, 1.0f),
                    (0.2f + (beat1 * beat2).toFloat() * 0.6f).coerceIn(0.1f, 1.0f),
                    (0.35f + (sin(step * 4.0) * 0.3f).toFloat()).coerceIn(0.1f, 1.0f)
                )

                delay(33) // ~30Hz update rate
            }
        }
    }

    private fun stopAudioSimulation() {
        animationJob?.cancel()
        animationJob = null
        _rhythmPulse.value = 1.0f
        _spectrumBands.value = floatArrayOf(0.4f, 0.6f, 0.5f, 0.3f)
    }
}
