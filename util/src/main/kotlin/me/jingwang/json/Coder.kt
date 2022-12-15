package me.jingwang.json

import com.codingrodent.jackson.crypto.CryptoModule
import com.codingrodent.jackson.crypto.EncryptionService
import com.codingrodent.jackson.crypto.PasswordCryptoContext
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr353.JSR353Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import me.jingwang.json.mask.MaskModule
import kotlin.reflect.KClass

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/21 6:18 PM
 *
 */
object Coder {
    val json = ObjectMapper().apply {
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
        registerModules(KotlinModule(), JSR353Module(), ParameterNamesModule(), Jdk8Module(), JavaTimeModule(), MaskModule())
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NON_PRIVATE)
        enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
        configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
        configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
        configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
        configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true)
//        logger.debug { "Jackson registered modules: $registeredModuleIds" }
    }

    fun <T: Any> jsonDecodeAsList(str: String, clz: KClass<T>): List<T> {
        val t = json.typeFactory.constructCollectionType(List::class.java, clz.java)
        return json.readValue(str, t)
    }
}

fun Any.jsonEncode() : String {
    return Coder.json.writeValueAsString(this)
}

fun Any.jsonPrettyEncode() : String {
    return Coder.json.writerWithDefaultPrettyPrinter().writeValueAsString(this)
}

inline fun <reified T> String.jsonDecode() : T {
    return Coder.json.readValue(this, T::class.java)
}

fun <T: Any> String.jsonDecode(clz: KClass<T>) : T {
    return Coder.json.readValue(this, clz.java)
}

fun <T: Any> String.jsonDecodeAsList(clz: KClass<T>): List<T> {
    val t = Coder.json.typeFactory.constructCollectionType(List::class.java, clz.java)
    return Coder.json.readValue(this, t)
}

inline fun <reified T: Any> String.jsonDecodeAsList() : List<T> {
    val type = Coder.json.typeFactory.constructCollectionType(List::class.java, T::class.java)
    return Coder.json.readValue(this, type)
}
