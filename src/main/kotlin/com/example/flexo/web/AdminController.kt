package com.example.flexo.web

import com.example.flexo.exercise.Exercise
import com.example.flexo.exercise.ExerciseRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/admin")
class AdminController(
    private val exerciseRepository: ExerciseRepository,
    private val objectMapper: ObjectMapper
) {

    private val log = LoggerFactory.getLogger(AdminController::class.java)

    /**
     * Exports all exercises as a JSON file.
     */
    @GetMapping("/export")
    fun exportExercises(@RequestParam("selectedProgramId", required = false) selectedProgramId: String?): ResponseEntity<ByteArray> {
        val exercises = if (!selectedProgramId.isNullOrBlank()) {
            exerciseRepository.findByProgramIdOrderBySortOrderAsc(selectedProgramId)
        } else {
            exerciseRepository.findAllByOrderBySortOrderAsc()
        }

        log.info("Exporting ${exercises.size} exercises (selectedProgramId=$selectedProgramId)")
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"exercises.json\"")
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(exercises))
    }

    /**
     * Imports exercises from a JSON file and assigns them to the selected program.
     */
    @PostMapping("/import")
    fun importExercises(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("selectedProgramId") selectedProgramId: String
    ): String {
        if (file.isEmpty) return "redirect:/?importError=File is empty"

        return try {
            val exercises = objectMapper.readValue<List<Exercise>>(file.bytes).map {
                it.copy(id = null, programId = selectedProgramId)
            }
            exerciseRepository.saveAll(exercises)
            log.info("Imported ${exercises.size} exercises with programId $selectedProgramId")
            "redirect:/"
        } catch (e: Exception) {
            log.error("Failed to import exercises", e)
            "redirect:/?importError=Invalid JSON format"
        }
    }
}