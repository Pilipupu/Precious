package me.jingwang.channel

import java.util.concurrent.locks.ReentrantLock

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/12/29 6:35 PM
 *
 */
class Channel {
    val lock = ReentrantLock()
    val condition = lock.newCondition()
}