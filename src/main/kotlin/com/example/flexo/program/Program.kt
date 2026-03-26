package com.example.flexo.program

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("programs")
data class Program(
    @Id
    val id: ObjectId? = null,
    val name: String,
    val description: String? = null,
    val exerciseIds: List<ObjectId> = emptyList()
)

