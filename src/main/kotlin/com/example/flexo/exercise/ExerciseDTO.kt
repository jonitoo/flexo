package com.example.flexo.exercise

data class ExerciseDTO(
    val id: String? = null,
    val name: String? = "",
    val description: String? = "",
    var timerDurationSeconds: Int = 60,
    val image: ByteArray? = null,
    val imageContentType: String? = null,
    val imageFilename: String? = null,

    ) {
    override fun toString(): String {
        return "ExerciseDTO(id=$id, name=$name, description=$description, timerDurationSeconds=$timerDurationSeconds)"
    }
}