package com.task.github.support.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.task.github.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader(context: Context?) {
    var memoryCache = MemoryCache()
    var fileCache: FileCache

    private val imageViews = Collections.synchronizedMap(
        WeakHashMap<ImageView, String>()
    )
    var executorService: ExecutorService



    val stubId = R.drawable.dafult_placeholder
    fun displayImage(url: String, imageView: ImageView) {
        imageViews[imageView] = url

        val bitmap = memoryCache[url]
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            queuePhoto(url, imageView)

            imageView.setImageResource(stubId)
        }
    }

    private fun queuePhoto(url: String, imageView: ImageView) {
        val p = PhotoToLoad(url, imageView)
        executorService.submit(PhotosLoader(p))
    }

    inner class PhotoToLoad(var url: String, var imageView: ImageView)
    internal inner class PhotosLoader(var photoToLoad: PhotoToLoad) : Runnable {
        override fun run() {
            try {
                if (imageViewReused(photoToLoad)) return
                getBitmap(photoToLoad.url)?.also {
                    memoryCache.put(photoToLoad.url, it)
                    if (imageViewReused(photoToLoad)) return

                    val bd = BitmapDisplayer(it, photoToLoad)

                    Handler(Looper.getMainLooper()).post(bd)
                }

                // set image data in Memory Cache

            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }

    private fun getBitmap(url: String): Bitmap? {
        val f = fileCache.getFile(url)
        val b = decodeFile(f)
        return b
            ?: try {
                var bitmap: Bitmap?
                val imageUrl = URL(url)
                val conn = imageUrl.openConnection() as HttpURLConnection
                conn.connectTimeout = 30000
                conn.readTimeout = 30000
                conn.instanceFollowRedirects = true
                val `is` = conn.inputStream

                val os: OutputStream = FileOutputStream(f)

                MemoryCache.CopyStream(`is`, os)
                os.close()
                conn.disconnect()

                bitmap = decodeFile(f)
                bitmap
            } catch (ex: Throwable) {
                ex.printStackTrace()
                if (ex is OutOfMemoryError) memoryCache.clear()
                null
            }

    }

    private fun decodeFile(f: File): Bitmap? {
        try {

            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(f)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()

            val REQUIRED_SIZE = 85
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val stream2 = FileInputStream(f)
            val bitmap = BitmapFactory.decodeStream(stream2, null, o2)
            stream2.close()
            return bitmap
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
        val tag = imageViews[photoToLoad.imageView]
        return if (tag == null || tag != photoToLoad.url) true else false
    }

    internal inner class BitmapDisplayer(var bitmap: Bitmap?, var photoToLoad: PhotoToLoad) :
        Runnable {
        override fun run() {
            if (imageViewReused(photoToLoad)) return

            // Show bitmap on UI
            if (bitmap != null) photoToLoad.imageView.setImageBitmap(bitmap) else photoToLoad.imageView.setImageResource(
                stubId
            )
        }
    }

    fun clearCache() {
        memoryCache.clear()
        fileCache.clear()
    }

    init {
        fileCache = FileCache(context!!)

        executorService = Executors.newFixedThreadPool(5)
    }
}