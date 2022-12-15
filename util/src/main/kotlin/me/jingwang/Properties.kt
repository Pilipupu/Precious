package me.jingwang

import java.net.URL
import java.util.*

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/27 1:47 PM
 *
 */


/**
 * do not add quota
 */
fun URL.toProperties() : Properties {
    val p = Properties()
    p.load(this.openStream())
    return p
}

fun getProperties(baseName: String) : ResourceBundle {
    var _baseName: String = baseName
    if (baseName.endsWith(".properties")) {
        _baseName = baseName.substringBeforeLast(".")
    }
    return ResourceBundle.getBundle(_baseName)
}

