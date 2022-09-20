import me.jingwang.aspectj.Student
import me.jingwang.aspectj.User
import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/20 5:45 PM
 *
 */
class TestAspectj {
    @Test
    fun testAspect() {
        User().read()
    }

    @Test
    fun testStudent() {
        Student().read()
    }
}