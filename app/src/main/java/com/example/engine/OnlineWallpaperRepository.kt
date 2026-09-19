package com.example.engine

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.example.model.OnlineWallpaper
import com.example.model.WallpaperCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * Repository to discover, download and apply online wallpapers from public APIs,
 * and handle custom device gallery images.
 */
object OnlineWallpaperRepository {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    // Curated high-res initial wallpapers for instant offline/online viewing
    private val curatedWallpapers = listOf(
        OnlineWallpaper(
            id = "curated_1",
            title = "iOS 18 Liquid Titanium",
            category = "iOS Minimal",
            author = "Apple Studio",
            thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_2",
            title = "Deep Ocean Glass Caustics",
            category = "Sıvı Cam & 3D",
            author = "Hydra Optics",
            thumbnailUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_3",
            title = "Nordic Fog & Mountains",
            category = "Doğa & Deniz",
            author = "Soren K.",
            thumbnailUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_4",
            title = "Neon Tokyo Horizon",
            category = "Siber & Neon",
            author = "Kento M.",
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_5",
            title = "Nebula Dark OLED",
            category = "Karanlık OLED",
            author = "NASA Hubble",
            thumbnailUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_6",
            title = "Minimal Pastel Dunes",
            category = "iOS Minimal",
            author = "Studio Minimal",
            thumbnailUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_7",
            title = "Bioluminescent Abyssal Bloom",
            category = "Sıvı Cam & 3D",
            author = "Aqua Bloom",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1440&q=95"
        ),
        OnlineWallpaper(
            id = "curated_8",
            title = "Alpine Lake Reflections",
            category = "Doğa & Deniz",
            author = "Lucas W.",
            thumbnailUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=400&q=80",
            downloadUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1440&q=95"
        )
    )

    /**
     * Fetches real online wallpapers using Picsum Photos API + categorized curated feeds.
     */
    suspend fun fetchWallpapers(category: WallpaperCategory = WallpaperCategory.ALL): List<OnlineWallpaper> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<OnlineWallpaper>()
            list.addAll(curatedWallpapers)

            try {
                val request = Request.Builder()
                    .url("https://picsum.photos/v2/list?page=1&limit=25")
                    .header("User-Agent", "TvnahLauncher/2026")
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        if (!body.isNullOrBlank()) {
                            val jsonArray = JSONArray(body)
                            for (i in 0 until jsonArray.length()) {
                                val item = jsonArray.getJSONObject(i)
                                val id = item.optString("id", "$i")
                                val author = item.optString("author", "Fotoğrafçı")
                                val downloadUrl = "https://picsum.photos/id/$id/1080/2340"
                                val thumbUrl = "https://picsum.photos/id/$id/400/800"

                                list.add(
                                    OnlineWallpaper(
                                        id = "picsum_$id",
                                        title = "$author Çekimi",
                                        category = if (i % 2 == 0) "Doğa & Deniz" else "iOS Minimal",
                                        author = author,
                                        thumbnailUrl = thumbUrl,
                                        downloadUrl = downloadUrl
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Return curated wallpapers if network fails
            }

            if (category == WallpaperCategory.ALL) {
                list
            } else {
                list.filter { it.category.contains(category.titleTr, ignoreCase = true) }
            }
        }

    /**
     * Downloads an online wallpaper and sets it directly as Android system wallpaper.
     */
    suspend fun downloadAndApplyWallpaper(
        context: Context,
        wallpaper: OnlineWallpaper,
        target: SystemWallpaperTarget
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(wallpaper.downloadUrl).build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Duvar kağıdı indirilemedi", Toast.LENGTH_SHORT).show()
                }
                return@withContext false
            }

            val inputStream: InputStream = response.body?.byteStream() ?: return@withContext false
            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Görüntü işlenemedi", Toast.LENGTH_SHORT).show()
                }
                return@withContext false
            }

            val wallpaperManager = WallpaperManager.getInstance(context)
            val whichFlag = when (target) {
                SystemWallpaperTarget.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
                SystemWallpaperTarget.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
                SystemWallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, whichFlag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }

            bitmap.recycle()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "✓ '${wallpaper.title}' ${target.labelTr} için sistem duvar kağıdı yapıldı!",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    /**
     * Applies an image picked from the user's device gallery as system wallpaper.
     */
    suspend fun applyGalleryImageAsWallpaper(
        context: Context,
        imageUri: Uri,
        target: SystemWallpaperTarget
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri) ?: return@withContext false
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Seçilen fotoğraf okunamadı", Toast.LENGTH_SHORT).show()
                }
                return@withContext false
            }

            val wallpaperManager = WallpaperManager.getInstance(context)
            val whichFlag = when (target) {
                SystemWallpaperTarget.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
                SystemWallpaperTarget.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
                SystemWallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, whichFlag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }

            bitmap.recycle()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "✓ Galeri fotoğrafı ${target.labelTr} duvar kağıdı yapıldı!",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }
}
