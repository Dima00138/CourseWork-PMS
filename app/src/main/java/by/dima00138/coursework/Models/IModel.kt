package by.dima00138.coursework.Models

import java.lang.reflect.Type
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

    fun getTypeOfField(key: String): Type {
        val fields = this::class.declaredMemberProperties.associateBy { it.name }
        val field = fields[key] ?: throw IllegalArgumentException("Field $key not found")
        return field.returnType.javaType
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