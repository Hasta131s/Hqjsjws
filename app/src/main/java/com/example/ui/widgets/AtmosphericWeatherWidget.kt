package com.example.ui.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WeatherState
import com.example.ui.liquid.LiquidGlassCard

@Composable
fun AtmosphericWeatherWidget(
    weatherState: WeatherState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        blurRefractionAlpha = 0.17f,
        glowAccentColor = Color(0xFF00E5FF),
        onClick = onRefresh
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF00F0FF),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = weatherState.city,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Text(
                    text = "AQI ${weatherState.airQualityIndex} • Temiz Hava",
                    fontSize = 11.sp,
                    color = Color(0xFF80DEEA),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.WbSunny,
                        contentDescription = "Weather Icon",
                        tint = Color(0xFFFFD166),
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${weatherState.temperatureCelsius}°C",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.White
                        )
                        Text(
                            text = weatherState.condition,
                            fontSize = 11.5.sp,
                            color = Color(0xFFE0F7FA)
                        )
                    }
                }

                // Weather metrics: Humidity & Wind
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Opacity,
                            contentDescription = "Humidity",
                            tint = Color(0xFF00F0FF),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%${weatherState.humidityPercent} Nem",
                            fontSize = 11.5.sp,
                            color = Color(0xFFCFD8DC)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Air,
                            contentDescription = "Wind",
                            tint = Color(0xFF80DEEA),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${weatherState.windSpeedKmh} km/s Rüzgar",
                            fontSize = 11.5.sp,
                            color = Color(0xFFCFD8DC)
                        )
                    }
                }
            }
        }
    }
}
