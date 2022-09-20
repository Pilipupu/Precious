import me.jingwang.aspectj.User;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : jingwang
 * @create 2022/9/21 9:59 AM
 */
public class JavaTest {
    @Test
    public void test() {
        User u = new User();
        u.read();
    }
}
