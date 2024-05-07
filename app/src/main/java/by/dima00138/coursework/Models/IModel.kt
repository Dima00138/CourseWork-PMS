package by.dima00138.coursework.Models

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaType

interface IModel {
    fun toFirebase() : Any
    fun getFields(): Map<String, Any> {
        return this::class.declaredMemberProperties.associate { prop ->
            prop.name to prop.returnType.javaType
        }
    }
    fun getField(key: String): Any {
        val fields = this::class.java.declaredFields.associateBy { it.name }
        val field = fields[key] ?: throw IllegalArgumentException("Field $key not found")
        field.isAccessible = true
        return field.get(this) ?: throw IllegalArgumentException("Field $key is null")
    }

    fun getAllFieldValues(): Map<String, Any?> {
        return this::class.declaredMemberProperties.associate { property ->
            property.name to property.getter.call(this)
        }
    }

    fun setField(key: String, value: Any): IModel {
        val fields = this::class.java.declaredFields.associateBy { it.name }
        val field = fields[key] ?: throw IllegalArgumentException("Field $key not found")
        field.isAccessible = true
        field.set(this, value)
        return this
    }
}