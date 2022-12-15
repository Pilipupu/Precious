package me.jingwang.test.bash

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*
import kotlin.concurrent.thread
import kotlin.reflect.full.memberFunctions

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/11/30 2:34 PM
 *
 */

//https://stackoverflow.com/questions/62325371/what-is-the-difference-between-different-processbuilder-redirectinput
class ProcessBuilderTest {
    @Test
    fun testBuildBash() {
        val pb = ProcessBuilder(listOf("/bin/bash", "-c", "ls /root"))
        pb.redirectError(ProcessBuilder.Redirect.INHERIT)
        val p = pb.start()
        println(p.errorStream == null)
        println(String(p.errorStream.readAllBytes()))
    }

    @Test
    fun testProcessPIPE() {
        val pb = ProcessBuilder(listOf("/bin/bash", "-c", "ls /root"))
        pb.redirectError(ProcessBuilder.Redirect.PIPE)
        val p = pb.start()
        println(p.errorStream == null)
        println(String(p.errorStream.readAllBytes()))
    }

    @Test
    fun testProcessSsh() {
        val pb = ProcessBuilder(listOf("/bin/bash", "-c", "ssh root@172.24.243.254 ls /usr/local/hyperconverged"))
        pb.redirectError(ProcessBuilder.Redirect.PIPE)
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE)
        val p = pb.start()
        val stdoutStream: ByteArrayOutputStream?
        val stderrStream: ByteArrayOutputStream?

        stdoutStream = ByteArrayOutputStream()
        stderrStream = ByteArrayOutputStream()

        val consumerThread = thread {
            runBlocking {
                launch {
                    val sc = Scanner(p.inputStream)
                    val ps = PrintStream(stdoutStream)
                    while (sc.hasNextLine()) {
                        ps.println(sc.nextLine())
                    }
                }

                launch {
                    val sc = Scanner(p.errorStream)
                    val ps = PrintStream(stderrStream)
                    while (sc.hasNextLine()) {
                        ps.println(sc.nextLine())
                    }
                }
            }
        }
        p.waitFor()

        consumerThread.join()
        val stdout = String(stdoutStream.toByteArray())
        val stderr = String(stderrStream.toByteArray())
        println(stdout)
        println(stderr)
    }

    @Test
    fun testReflect() {
        class A {
            fun call() : String {
                throw RuntimeException("test exception")
            }
        }

        val function = A::class.memberFunctions.find { it.name == "call" }!!
        try {
            function.call(A())
        } catch (e: Exception) {
            throw e.cause!!
        }
    }
}

fun main() {
    ProcessBuilderTest().testProcessSsh()
}