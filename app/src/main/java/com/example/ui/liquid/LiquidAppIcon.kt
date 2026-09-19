package com.example.ui.liquid

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppInfo
import com.example.model.IconShape
import com.example.model.IconThemePack

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LiquidAppIcon(
    app: AppInfo,
    iconShape: IconShape,
    iconThemePack: IconThemePack,
    iconSize: Dp = 56.dp,
    showLabel: Boolean = true,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val shape = remember(iconShape) { LiquidGlassTheme.getShapeForIcon(iconShape) }
    val imageBitmap = remember(app.icon) {
        app.icon?.let { drawableToBitmap(it) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(iconSize + 22.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 4.dp)
            .testTag("app_icon_${app.packageName}")
    ) {
        // Liquid Glass Icon Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize)
                .clip(shape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            iconThemePack.primaryTint.copy(alpha = 0.28f),
                            iconThemePack.secondaryTint.copy(alpha = 0.18f),
                            Color(0x20000000)
                        )
                    )
                )
                .border(
                    BorderStroke(
                        width = 1.2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.65f),
                                iconThemePack.primaryTint.copy(alpha = 0.8f),
                                iconThemePack.glowColor.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.4f)
                            )
                        )
                    ),
                    shape = shape
                )
                .drawBehind {
                    // Droplet specular lens highlight on top half
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.42f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.35f, size.height * 0.28f),
                            radius = size.width * 0.48f
                        ),
                        center = Offset(size.width * 0.35f, size.height * 0.28f),
                        radius = size.width * 0.48f
                    )
                }
                .padding(iconSize * 0.16f)
        ) {
            if (imageBitmap != null) {
                androidx.compose.foundation.Image(
                    painter = BitmapPainter(imageBitmap.asImageBitmap()),
                    contentDescription = app.label,
                    modifier = Modifier.size(iconSize * 0.72f)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Android,
                    contentDescription = app.label,
                    tint = iconThemePack.primaryTint,
                    modifier = Modifier.size(iconSize * 0.7f)
                )
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = app.label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(iconSize + 18.dp)
            )
        }
    }
}

private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable && drawable.bitmap != null) {
        return drawable.bitmap
    }
    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 96
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 96
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
