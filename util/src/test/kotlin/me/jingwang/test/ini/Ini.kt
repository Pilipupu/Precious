package me.jingwang.test.ini

import org.ini4j.Config
import org.ini4j.Wini
import org.junit.Test
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/11/16 3:54 PM
 *
 */
class Ini {
    @Test
    fun testCreateAnsibleInventoryFile() {
        val f = File.createTempFile("inv", ".tmp")
        val ini = Wini(f)
        ini.put("all", "ansible_host", "test2")
        ini.store()
        println(f.readText())
        f.delete()
    }
}