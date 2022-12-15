package me.jingwang.json.mask

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.module.SimpleModule
import java.util.*

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/10/12 5:58 PM
 *
 */
class MaskModule : SimpleModule() {
    private val serialVersionUID = 1L

    val GROUP_ID = "me.jingwang.json.mask"
    val ARTIFACT_ID = "jackson-json-mask"

    private val BUNDLE = MaskModule::class.java.getPackage().name + ".config"

    private var major = 0
    private var minor = 0
    private var patch = 0

    init {
        val rb = ResourceBundle.getBundle(BUNDLE)
        major = rb.getString("projectVersionMajor").toInt()
        minor = rb.getString("projectVersionMinor").toInt()
        patch = rb.getString("projectVersionBuild").toInt()
    }

    override fun version(): Version {
        return Version(major, minor, patch, null, GROUP_ID, ARTIFACT_ID)
    }

    override fun getModuleName(): String {
        return ARTIFACT_ID
    }

    override fun setupModule(context: SetupContext) {
        context.addBeanSerializerModifier(MaskSerializerModifier())
//        context.addBeanDeserializerModifier(deserializerModifierModifier)
    }
}