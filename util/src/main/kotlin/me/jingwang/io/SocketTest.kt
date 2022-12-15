package me.jingwang.io

import org.junit.Test
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket


/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2023/2/23 1:38 PM
 *
 */
class SocketTest {
    @Test
    fun testSocketClient() {
        try {
            val socket = Socket("localhost", 12345)
            socket.soTimeout = 3000
            println("Connected to server")
            val `in` = DataInputStream(socket.getInputStream())
            val out = DataOutputStream(socket.getOutputStream())
            out.writeUTF("Hello from client")
            val message = `in`.readUTF()
            println("Received message: $message")

            val message2 = `in`.readUTF()
            println("Received message: $message2")

            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun testSocketServer() {
        try {
            val server = ServerSocket(12345)
            println("Server started")
            while (true) {
                val socket = server.accept()
                println("Client connected")
                val `in` = DataInputStream(socket.getInputStream())
                val out = DataOutputStream(socket.getOutputStream())
                val message = `in`.readUTF()
                println("Received message: $message")
                out.writeUTF("Thank you for connecting")
            }
//            out.writeUTF("Thank you for connecting")
//            socket.close()
//            server.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}