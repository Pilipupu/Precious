package path

import me.jingwang.getProperties
import me.jingwang.io.PathUtil
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/27 1:41 PM
 *
 */
class GuavaResourceTest {
    @Test
    fun testGetResources() {
        val f = PathUtil.getResources("test.properties")
        val p = Properties()
        p.load(f.openStream())
        println(p.getProperty("TEST"))
    }

    @Test
    fun testResourceBundle() {
        val p  = getProperties("test")
        println(p.getString("TEST"))

        val p1  = getProperties("test.properties")
        println(p1.getString("TEST"))
    }

    @Test
    fun testTryWith() {
        null ?: println("aa")
    }
}