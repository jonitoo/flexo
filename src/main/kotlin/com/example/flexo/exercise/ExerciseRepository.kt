package com.example.flexo.exercise

import org.springframework.data.mongodb.repository.MongoRepository

interface ExerciseRepository : MongoRepository<Exercise, String> {
	fun findAllByOrderBySortOrderAsc(): List<Exercise>
	fun findByProgramIdOrderBySortOrderAsc(programId: String): List<Exercise>
}
