package com.task.github.support.imageLoader

import android.content.Context
import java.io.File

class FileCache(context: Context) {
    private var cacheDir: File = context.cacheDir

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    fun getFile(url: String): File {
        val filename = url.hashCode().toString()
        return File(cacheDir, filename)
    }

    fun clear() {
        val files: Array<File> = cacheDir.listFiles() ?: return
        for (f in files) f.delete()
    }
}