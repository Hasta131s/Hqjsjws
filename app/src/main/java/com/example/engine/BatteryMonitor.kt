package com.example.engine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.model.BatteryState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BatteryMonitor(private val context: Context) {

    fun observeBatteryState(): Flow<BatteryState> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val percent = if (level >= 0 && scale > 0) (level * 100) / scale else 80

                    val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL

                    val tempRaw = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
                    val tempCelsius = tempRaw / 10f

                    val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)

                    val healthRaw = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD)
                    val healthStr = when (healthRaw) {
                        BatteryManager.BATTERY_HEALTH_GOOD -> "Mükemmel"
                        BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Aşırı Isınma"
                        BatteryManager.BATTERY_HEALTH_DEAD -> "Tükenmiş"
                        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Yüksek Voltaj"
                        else -> "İyi"
                    }

                    trySend(
                        BatteryState(
                            levelPercent = percent,
                            isCharging = isCharging,
                            temperatureCelsius = if (tempCelsius > 0f) tempCelsius else 29.4f,
                            health = healthStr,
                            voltageMv = if (voltage > 0) voltage else 4120
                        )
                    )
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val stickyIntent = context.registerReceiver(receiver, filter)
        if (stickyIntent != null) {
            val level = stickyIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 85)
            val scale = stickyIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val percent = if (level >= 0 && scale > 0) (level * 100) / scale else 85
            val status = stickyIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            val tempRaw = stickyIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 290)
            val voltage = stickyIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4150)

            trySend(
                BatteryState(
                    levelPercent = percent,
                    isCharging = isCharging,
                    temperatureCelsius = tempRaw / 10f,
                    health = "Mükemmel",
                    voltageMv = voltage
                )
            )
        }

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (_: Exception) {}
        }
    }
}
