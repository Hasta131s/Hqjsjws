package com.example.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.liquid.LiquidGlassCard

@Composable
fun QuickControlsWidget(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var wifiEnabled by remember { mutableStateOf(true) }
    var bluetoothEnabled by remember { mutableStateOf(true) }
    var torchEnabled by remember { mutableStateOf(false) }
    var soundEnabled by remember { mutableStateOf(true) }

    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        blurRefractionAlpha = 0.16f
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlPill(
                icon = Icons.Rounded.Wifi,
                label = "Wi-Fi",
                isActive = wifiEnabled,
                onClick = { wifiEnabled = !wifiEnabled }
            )

            ControlPill(
                icon = Icons.Rounded.Bluetooth,
                label = "BT",
                isActive = bluetoothEnabled,
                onClick = { bluetoothEnabled = !bluetoothEnabled }
            )

            ControlPill(
                icon = Icons.Rounded.FlashlightOn,
                label = "Flaş",
                isActive = torchEnabled,
                activeColor = Color(0xFFFFD166),
                onClick = { torchEnabled = !torchEnabled }
            )

            ControlPill(
                icon = Icons.Rounded.VolumeUp,
                label = "Ses",
                isActive = soundEnabled,
                onClick = { soundEnabled = !soundEnabled }
            )

            ControlPill(
                icon = Icons.Rounded.Settings,
                label = "Ayar",
                isActive = false,
                onClick = onOpenSettings
            )
        }
    }
}

@Composable
private fun ControlPill(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color = Color(0xFF00F0FF),
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) activeColor.copy(alpha = 0.28f)
                    else Color.White.copy(alpha = 0.08f)
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) activeColor else Color(0xFFB0BEC5),
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isActive) Color.White else Color(0xFF90A4AE)
        )
    }
}
