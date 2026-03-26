package com.example.flexo.exercise

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Exercise(
    @Id
    val id: ObjectId? = null,
    val name: String? = "",
    val description: String? = "",
    var sortOrder: Int = 0,
    var timerDurationSeconds: Int = 60, // Default to 60 seconds
    val programId: String? = null,
    val image: ByteArray? = null,
    val imageContentType: String? = null,
    val imageFilename: String? = null
) {
    override fun toString(): String {
        return "Exercise(id=$id, name=$name, description=$description, imageFilename=$imageFilename)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Exercise) return false

        return id == other.id &&
                name == other.name &&
                description == other.description &&
                sortOrder == other.sortOrder &&
                timerDurationSeconds == other.timerDurationSeconds &&
                programId == other.programId &&
                imageContentType == other.imageContentType &&
                imageFilename == other.imageFilename
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + sortOrder
        result = 31 * result + timerDurationSeconds
        result = 31 * result + (programId?.hashCode() ?: 0)
        result = 31 * result + (imageContentType?.hashCode() ?: 0)
        result = 31 * result + (imageFilename?.hashCode() ?: 0)
        return result
    }
}