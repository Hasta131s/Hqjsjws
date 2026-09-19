package com.example.ui.ios

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.model.AppInfo

/**
 * Authentic Apple iOS SpringBoard App Icon.
 * Features Apple's squircle curvature (22.5% radius), subtle ambient elevation,
 * and high-contrast shadowed labels.
 */
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun IosAppIcon(
    app: AppInfo,
    modifier: Modifier = Modifier,
    iconSize: Dp = 60.dp,
    fontFamily: FontFamily = FontFamily.Default,
    showLabel: Boolean = true,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // iOS Spring tap bounce
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "ios_icon_spring"
    )

    // Authentic iOS corner radius is ~22.5% of the icon width
    val cornerRadius = iconSize * 0.225f
    val iconShape = RoundedCornerShape(cornerRadius)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(iconSize + 16.dp)
            .scale(scale)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag("ios_app_icon_${app.packageName}")
    ) {
        // iOS Squircle Icon Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize)
                .shadow(
                    elevation = 6.dp,
                    shape = iconShape,
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                )
                .clip(iconShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2C2C2E),
                            Color(0xFF1C1C1E)
                        )
                    )
                )
                .border(0.5.dp, Color.White.copy(alpha = 0.15f), iconShape)
        ) {
            val bitmap = remember(app.icon) {
                app.icon?.toBitmap(128, 128)
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = app.label,
                    modifier = Modifier
                        .size(iconSize)
                        .clip(iconShape)
                )
            } else {
                Text(
                    text = app.label.take(1).uppercase(),
                    color = Color.White,
                    fontSize = (iconSize.value * 0.42f).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily
                )
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = app.label,
                fontSize = 11.5.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.85f),
                        offset = Offset(0f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}
