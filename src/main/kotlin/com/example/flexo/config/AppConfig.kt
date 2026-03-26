package com.example.flexo.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class ObjectIdSerializer : JsonSerializer<ObjectId>() {
    override fun serialize(value: ObjectId?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.toHexString())
    }
}

class ObjectIdDeserializer : JsonDeserializer<ObjectId>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ObjectId {
        return ObjectId(p?.text)
    }
}

@Configuration
class AppConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        val module = SimpleModule()
        module.addSerializer(ObjectId::class.java, ObjectIdSerializer())
        module.addDeserializer(ObjectId::class.java, ObjectIdDeserializer())
        objectMapper.registerModule(module)
        return objectMapper
    }
}
