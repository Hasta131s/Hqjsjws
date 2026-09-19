package com.example.engine

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the device physical camera flash (torch) for iOS-style quick flashlight toggle.
 */
object DeviceFlashlightManager {
    private val _isTorchOn = MutableStateFlow(false)
    val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

    private var torchCallbackRegistered = false

    fun init(context: Context) {
        if (torchCallbackRegistered) return
        try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager ?: return
            cameraManager.registerTorchCallback(object : CameraManager.TorchCallback() {
                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    super.onTorchModeChanged(cameraId, enabled)
                    _isTorchOn.value = enabled
                }
            }, null)
            torchCallbackRegistered = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleTorch(context: Context): Boolean {
        init(context)
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        if (cameraManager == null) {
            Toast.makeText(context, "Kamera servisi bulunamadı", Toast.LENGTH_SHORT).show()
            return false
        }

        vibrateClick(context)

        return try {
            val cameraId = getCameraIdWithFlash(cameraManager)
            if (cameraId == null) {
                Toast.makeText(context, "Cihazda flaş donanımı bulunamadı", Toast.LENGTH_SHORT).show()
                return false
            }

            val newMode = !_isTorchOn.value
            cameraManager.setTorchMode(cameraId, newMode)
            _isTorchOn.value = newMode
            newMode
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Toast.makeText(context, "Fener açılamadı: ${e.message}", Toast.LENGTH_SHORT).show()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getCameraIdWithFlash(cameraManager: CameraManager): String? {
        for (id in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (hasFlash && facing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }
        }
        return cameraManager.cameraIdList.firstOrNull()
    }

    private fun vibrateClick(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(35)
                }
            }
        } catch (e: Exception) {
            // Ignore vibration errors
        }
    }
}
