package me.jingwang.test.network

import me.jingwang.json.jsonDecodeAsList
import me.jingwang.json.jsonEncode
import me.jingwang.json.jsonPrettyEncode
import me.jingwang.network.Network
import org.junit.Test
import java.net.NetworkInterface

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/11/2 1:32 PM
 *
 */
class NetworkTest {
    @Test
    fun testGetEtherDevices() {
        val devices = Network.getLocalEtherDevices()
        val json = devices.jsonPrettyEncode()
        println(json)
        val lst = json.jsonDecodeAsList(NetworkInterface::class)
        println(lst.size)
    }

    @Test
    fun testThreeEyes() {
        println(this.javaClass)
        println(this.javaClass.name)
        println(this.javaClass.simpleName)
        println(this.javaClass.canonicalName)
    }

    @Test
    fun testPing() {
        while (true) {
            Network.isRemotePortOpen("172.24.243.232", 22, 1000)
            Thread.sleep(3000)
        }
    }
}