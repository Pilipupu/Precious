package me.jingwang.json.mask

import com.codingrodent.jackson.crypto.EncryptedJson
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.StringWriter
import java.nio.charset.StandardCharsets

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/12 6:40 PM
 *
 */
class MaskJsonSerializer : JsonSerializer<Any>() {
    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeString("******")
    }
}