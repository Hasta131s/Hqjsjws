package com.example.ui.wallpaper

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.engine.SystemWallpaperTarget
import com.example.model.OnlineWallpaper
import com.example.model.WallpaperCategory
import com.example.ui.liquid.LiquidGlassCard

@Composable
fun OnlineWallpaperSheet(
    isOpen: Boolean,
    wallpapers: List<OnlineWallpaper>,
    isLoading: Boolean,
    selectedCategory: WallpaperCategory,
    previewWallpaper: OnlineWallpaper?,
    onClose: () -> Unit,
    onSelectCategory: (WallpaperCategory) -> Unit,
    onSelectPreview: (OnlineWallpaper?) -> Unit,
    onApplyOnlineWallpaper: (OnlineWallpaper, SystemWallpaperTarget) -> Unit,
    onPickGalleryImage: (Uri, SystemWallpaperTarget) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Zero-permission Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onPickGalleryImage(uri, SystemWallpaperTarget.BOTH)
        }
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xF0071324),
                            Color(0xF8050D18),
                            Color(0xFF03070E)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF00F0FF).copy(alpha = 0.20f))
                                .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CloudDownload,
                                contentDescription = "Online Wallpapers",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Çevrimiçi Duvar Kağıtları",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "HD & 4K İndirilebilir Canlı Koleksiyon",
                                fontSize = 12.sp,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Kapat",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Bar: Pick from Device Gallery
                LiquidGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    blurRefractionAlpha = 0.22f,
                    glowAccentColor = Color(0xFF00F0FF)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.AddPhotoAlternate,
                                contentDescription = "Galeri",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Cihaz Galerisinden Fotoğraf Seç",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Telefonundaki herhangi bir görseli duvar kağıdı yap",
                                    fontSize = 11.sp,
                                    color = Color(0xFF90A4AE)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Rounded.Wallpaper,
                            contentDescription = "Seç",
                            tint = Color(0xFF00F0FF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Categories Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(WallpaperCategory.values()) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCategory(cat) },
                            label = {
                                Text(
                                    text = cat.titleTr,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White.copy(alpha = 0.07f),
                                labelColor = Color(0xFFB0BEC5),
                                selectedContainerColor = Color(0xFF00F0FF).copy(alpha = 0.25f),
                                selectedLabelColor = Color(0xFF00F0FF)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) Color(0xFF00F0FF) else Color.White.copy(alpha = 0.1f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Content Grid
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = Color(0xFF00F0FF),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Duvar kağıtları yükleniyor...",
                                fontSize = 13.sp,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(wallpapers) { wallpaper ->
                            WallpaperCard(
                                wallpaper = wallpaper,
                                onClick = { onSelectPreview(wallpaper) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Fullscreen Preview & Apply Dialog
    if (previewWallpaper != null) {
        WallpaperPreviewDialog(
            wallpaper = previewWallpaper,
            onDismiss = { onSelectPreview(null) },
            onApply = { target ->
                onApplyOnlineWallpaper(previewWallpaper, target)
                onSelectPreview(null)
            }
        )
    }
}

@Composable
private fun WallpaperCard(
    wallpaper: OnlineWallpaper,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.62f)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(wallpaper.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = wallpaper.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for author & title
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = wallpaper.title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "📷 ${wallpaper.author}",
                fontSize = 10.sp,
                color = Color(0xFF80DEEA),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun WallpaperPreviewDialog(
    wallpaper: OnlineWallpaper,
    onDismiss: () -> Unit,
    onApply: (SystemWallpaperTarget) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Full resolution preview
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(wallpaper.downloadUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = wallpaper.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Kapat",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Bottom Action Bar
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
                    .navigationBarsPadding()
                    .padding(20.dp)
            ) {
                Text(
                    text = wallpaper.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Fotoğrafçı: ${wallpaper.author} • ${wallpaper.category}",
                    fontSize = 12.sp,
                    color = Color(0xFF80DEEA)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sistem Duvar Kağıdı Olarak Uygula:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onApply(SystemWallpaperTarget.HOME_SCREEN) },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Ana Ekran", fontSize = 11.5.sp)
                    }

                    Button(
                        onClick = { onApply(SystemWallpaperTarget.LOCK_SCREEN) },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Kilit", fontSize = 11.5.sp)
                    }

                    Button(
                        onClick = { onApply(SystemWallpaperTarget.BOTH) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00F0FF),
                            contentColor = Color(0xFF00202E)
                        )
                    ) {
                        Text("Her İkisi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
