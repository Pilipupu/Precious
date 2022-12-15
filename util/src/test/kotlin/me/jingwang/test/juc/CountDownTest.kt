package me.jingwang.test.juc

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/11/8 5:37 PM
 *
 */
class CountDownTest {
    @Test
    fun testCountDown() {
        val countDownLatch = CountDownLatch(5)
        for (i in 1..4) {
            thread {
                println(i)
                TimeUnit.SECONDS.sleep(i.toLong())
                countDownLatch.countDown()
            }
        }
        println("before count down")
        countDownLatch.await()
        println("finish")
    }
}