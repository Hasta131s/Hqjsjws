package com.example.ui.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppInfo

/**
 * Confirmation dialog to either remove app from Home Screen or uninstall it completely from the device.
 */
@Composable
fun AppRemoveOrDeleteDialog(
    app: AppInfo,
    fontFamily: FontFamily,
    onRemoveFromHome: () -> Unit,
    onUninstallDevice: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF222328))
                .border(0.5.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "\"${app.label}\" Kaldırılsın mı?",
                    fontSize = 17.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bu uygulamayı ana ekrandan gizleyebilir veya cihazdan tamamen silebilirsiniz.",
                    fontSize = 12.sp,
                    fontFamily = fontFamily,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 1. ANA EKRANDAN KALDIR (Keep in App Library)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable {
                            onRemoveFromHome()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp, horizontal = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.RemoveCircleOutline,
                            contentDescription = null,
                            tint = Color(0xFF007AFF),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Ana Ekrandan Kaldır",
                                fontSize = 13.5.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF007AFF)
                            )
                            Text(
                                text = "Uygulama Arşivi'nde kalmaya devam eder",
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.55f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. UYGULAMAYI SİL (Uninstall completely)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF453A).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFFF453A).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .clickable {
                            onUninstallDevice()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp, horizontal = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteForever,
                            contentDescription = null,
                            tint = Color(0xFFFF453A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Uygulamayı Sil (Kaldır)",
                                fontSize = 13.5.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFF453A)
                            )
                            Text(
                                text = "Uygulama ve tüm verileri cihazdan silinir",
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                color = Color(0xFFFF453A).copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // İPTAL
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Vazgeç",
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
