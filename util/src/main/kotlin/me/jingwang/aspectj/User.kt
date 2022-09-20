package me.jingwang.aspectj

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/20 5:36 PM
 *
 */
class User {
    @LogAnno
    fun read() {
        println("reading")
    }
}