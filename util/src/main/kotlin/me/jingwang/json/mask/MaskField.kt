package me.jingwang.json.mask

import com.fasterxml.jackson.annotation.JacksonAnnotation
import java.lang.annotation.ElementType
import javax.swing.text.Element

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/12 5:55 PM
 *
 */
@JacksonAnnotation
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MaskField()
