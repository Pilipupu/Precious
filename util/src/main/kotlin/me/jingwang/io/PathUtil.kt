package me.jingwang.io

import com.google.common.io.Resources
import java.io.File
import java.net.URISyntaxException
import java.net.URL

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/23 10:20 AM
 *
 */
object PathUtil {
    fun findFileOnClassPath(path: String) : File? {
        val fileURL = PathUtil.javaClass.classLoader.getResource(path)
        if (fileURL == null || "file" != fileURL.protocol) {
            return null
        }
        return try {
            File(fileURL.toURI())
        } catch (e: URISyntaxException) {
            null
        }
    }

    fun getResources(fileName: String) : URL {
        return Resources.getResource(fileName)
    }
}