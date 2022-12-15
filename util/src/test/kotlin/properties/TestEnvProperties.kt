package properties

import me.jingwang.io.PathUtil
import org.junit.Test
import java.io.FileInputStream
import java.util.*

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/23 9:56 AM
 *
 */
class TestEnvProperties {
    @Test
    fun getProperties() {
        while (true) {
            val f = PathUtil.findFileOnClassPath("test.properties")
            val p = Properties().apply {
                load(FileInputStream(f))
            }
            println(p.getProperty("TEST"))
            println(p.getProperty("TEST1"))
            println(p.getProperty("TEST2"))
            Thread.sleep(1000)
        }
    }
}