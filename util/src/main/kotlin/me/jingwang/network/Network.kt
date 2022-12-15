package me.jingwang.network

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketException
import java.util.logging.Logger

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/11/2 1:30 PM
 *
 */
object Network {
    val logger = LoggerFactory.getLogger(javaClass)

    fun getLocalEtherDevices() : List<NetworkInterface> {
        return NetworkInterface.getNetworkInterfaces().toList()
    }

    fun isRemotePortOpen(ip: String, port: Int, timeout: Int = 1000): Boolean {
        val socket = Socket()
        socket.use {
            return try {
                socket.reuseAddress = true
                val sa = InetSocketAddress(ip, port)
                socket.connect(sa, timeout)
                socket.isConnected
            } catch (e: SocketException) {
                logger.trace(String.format("unable to connect remote port[ip:%s, port:%s], %s", ip, port, e.message), e)
                false
            } catch (e: IOException) {
                logger.trace(String.format("unable to connect remote port[ip:%s, port:%s], %s", ip, port, e.message), e)
                false
            } finally {
                try {
                    socket.close()
                } catch (e: IOException){
                    logger.error(e.message)
                }
            }
        }
    }
}