package me.jingwang.db

import com.alibaba.druid.pool.DruidDataSourceFactory
import io.ktor.http.*
import me.jingwang.io.PathUtil
import me.jingwang.toProperties
import java.util.*

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/27 1:24 PM
 *
 */
object DB {
    val dataSource = DruidDataSourceFactory.createDataSource(Properties().apply {
        val dbProperties = PathUtil.getResources("db.properties").toProperties()
        setProperty("url", dbProperties.getProperty("url"))
        setProperty("username", dbProperties.getProperty("username"))
        setProperty("password", dbProperties.getProperty("password"))
        setProperty("maxActive", dbProperties.getProperty("maxActive"))
        setProperty("initialSize", dbProperties.getProperty("initialSize"))
        setProperty("timeBetweenEvictionRunsMillis", dbProperties.getProperty("timeBetweenEvictionRunsMillis"))
        setProperty("validationQuery", "select 1")
        setProperty("testWhileIdle", "true")
        setProperty("maxWait","5000")
    })
}