package db

import me.jingwang.db.DB
import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/27 2:02 PM
 *
 */
class TestDataSource {
    // GRANT ALL PRIVILEGES ON *.* TO 'zops'@'%' IDENTIFIED BY ''  WITH GRANT OPTION;
    @Test
    fun testCreateDataSource() {
        try {
            println(DB.dataSource.connection)
        } catch (e: Exception) {
            println(false)
        }
    }
}