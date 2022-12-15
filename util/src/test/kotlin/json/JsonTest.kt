package json

import kotlinx.atomicfu.locks.reentrantLock
import me.jingwang.json.Coder
import me.jingwang.json.jsonDecode
import me.jingwang.json.jsonEncode
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import kotlin.concurrent.thread

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/26 9:56 AM
 *
 */
class JsonTest {
    @Test
    fun testJsonEncodeTwice() {
        val u = User("wang", "password")
        val j1 = Coder.json.writeValueAsString(u)
        println(j1)
        val j2 = Coder.json.writeValueAsString(j1)
        println(j2)
        println(j2.substring(1 until j2.length-1))
    }

    @Test
    fun testJsonEncodeAndDecode() {
        val u = User("wang", "password")
        val j1 = u.jsonEncode()
        println(j1)
        val user1 = j1.jsonDecode(User1::class)
        println(user1.jsonEncode())
        val j2 = user1.jsonEncode()
        println(j2)
        val u2 = j2.jsonDecode(User::class)
        println(u2.jsonEncode())
    }

    @Test
    fun testParal() {
        val a= println(JsonTest::class)

        thread {
            a()
        }
        thread {
            a()
        }
        while (true) {

        }
    }
    fun a() {
        synchronized(JsonTest::class) {
            println("aaaaaa")
            TimeUnit.SECONDS.sleep(10)
        }
    }
}

data class User(
    val name: String,
    val password: String
)

data class User1(
    val name: String,
    val password: String,
    var age: Int = 18
)