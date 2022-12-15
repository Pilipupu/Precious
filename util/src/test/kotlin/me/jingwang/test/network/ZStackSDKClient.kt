package me.jingwang.test.network

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2023/2/2 11:09 AM
 *
 */
class ZStackSDKClient {
    @Test
    fun testZStackApi() {
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .callTimeout(3, TimeUnit.SECONDS)
            .build()

        val retry = "%s"
        val retryMaxTime = "%s"
        val host = "172.20.14.145"
        val port = "8080".toInt()

        val url = "http://$host:$port/zstack/api"

        val JSON = MediaType.get("application/json; charset=utf-8")
        val json = "{\"org.zstack.header.apimediator.APIIsReadyToGoMsg\": {}}"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        println(response.isSuccessful)
    }
}