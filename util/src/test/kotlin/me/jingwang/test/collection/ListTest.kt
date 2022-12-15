package me.jingwang.test.collection

import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/11/8 4:50 PM
 *
 */
class ListTest {
    @Test
    fun testListRemove() {
        val l = mutableListOf(0,1,2,3,4)
        val it = l.iterator()
        it.next()
        it.remove()
        println(l)
    }

    @Test
    fun testRemoveWhenForEach() {
        val l = mutableListOf(0,1,2,3,4)
        l.forEach {
            if (it == 2) {
                l.removeAt(3)
            }
        }
    }

    @Test
    fun testPrint() {
        listOf<String>().let {
            println(it)
        }
    }
}