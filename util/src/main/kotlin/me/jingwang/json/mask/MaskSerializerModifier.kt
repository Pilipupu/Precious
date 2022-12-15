package me.jingwang.json.mask

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/12 6:20 PM
 *
 */
class MaskSerializerModifier : BeanSerializerModifier() {
    override fun changeProperties(
        config: SerializationConfig?,
        beanDesc: BeanDescription?,
        beanProperties: MutableList<BeanPropertyWriter>?
    ): MutableList<BeanPropertyWriter> {
        val newWriters = mutableListOf<BeanPropertyWriter>()
        beanProperties?.forEach { writer ->
            if (writer.getAnnotation(MaskField::class.java) == null) {
                newWriters.add(writer)
            } else {
                newWriters.add(MaskPropertyWriter(writer, MaskJsonSerializer()))
            }
        }
        return newWriters
    }
}

class MaskPropertyWriter(base : BeanPropertyWriter) : BeanPropertyWriter(base) {
    constructor(base : BeanPropertyWriter, serializer: JsonSerializer<Any>) : this(base) {
        this._serializer = serializer
    }
}