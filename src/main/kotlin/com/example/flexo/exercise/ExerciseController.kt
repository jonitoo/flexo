package com.example.flexo.exercise

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/")
class ExerciseController(
    private val exerciseRepository: ExerciseRepository
) {
    private val log = LoggerFactory.getLogger(ExerciseController::class.java)

    @PostMapping("/add")
    fun add(@ModelAttribute exerciseDTO: ExerciseDTO?, model: Model, session: HttpSession): String {
        val selectedProgramId = session.getAttribute("selectedProgramId") as? String
        val maxSortOrder = selectedProgramId?.let {
            exerciseRepository.findByProgramIdOrderBySortOrderAsc(it).maxOfOrNull { it.sortOrder } ?: 0
        } ?: exerciseRepository.findAll().maxOfOrNull { it.sortOrder } ?: 0

        val saved = exerciseRepository.save(
            Exercise(
                name = exerciseDTO?.name ?: throw IllegalArgumentException("ExerciseDTO cannot be null"),
                description = exerciseDTO.description,
                sortOrder = maxSortOrder + 1,
                programId = selectedProgramId,
                timerDurationSeconds = exerciseDTO.timerDurationSeconds
            )
        )
        model.addAttribute("exercise", saved)
        return "fragments/exercises :: exercise-row"
    }

    @PostMapping("/edit")
    fun edit(@ModelAttribute exerciseDTO: ExerciseDTO?, model: Model): String {
        val existing = exerciseRepository.findById(
            exerciseDTO?.id ?: throw IllegalArgumentException("ExerciseDTO ID cannot be null")
        ).orElseThrow { IllegalArgumentException("Exercise not found") }
        val updated = existing.copy(
            name = exerciseDTO.name,
            description = exerciseDTO.description,
            timerDurationSeconds = exerciseDTO.timerDurationSeconds
        )
        exerciseRepository.save(updated)
        model.addAttribute("exercise", updated)
        return "fragments/exercises :: exercise-row"
    }

    @GetMapping("/edit/{id}")
    fun showEditForm(@PathVariable id: String, model: Model): String {
        val exercise = exerciseRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Exercise with id $id not found") }
        model.addAttribute("exercise", exercise)
        return "fragments/exercises :: edit-exercise-modal-content"
    }

    @DeleteMapping("/delete")
    @ResponseBody
    fun delete(@RequestParam id: String) {
        exerciseRepository.deleteById(id)
        log.info("Deleted exercise id={}", id)
    }

    @PostMapping("/reorder")
    fun reorder(@RequestParam("idx") sortIds: List<String>, model: Model, session: HttpSession): String {
        sortIds.forEachIndexed { index, id ->
            exerciseRepository.findById(id).ifPresent {
                it.sortOrder = index
                exerciseRepository.save(it)
            }
        }
        val selectedProgramId = session.getAttribute("selectedProgramId") as? String
        val exercises = selectedProgramId?.let {
            exerciseRepository.findByProgramIdOrderBySortOrderAsc(it)
        } ?: exerciseRepository.findAllByOrderBySortOrderAsc()
        model.addAttribute("exercises", exercises)
        return "fragments/exercises :: exercise-table-body"
    }

    @PostMapping("/exercise/{id}/image")
    fun uploadImage(
        @PathVariable id: String,
        @RequestParam("image") image: MultipartFile,
        model: Model
    ): String {
        val existing = exerciseRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Exercise not found") }
        if (image.isEmpty) {
            model.addAttribute("exercise", existing)
            return "fragments/exercises :: exercise-row"
        }
        val updated = existing.copy(
            image = image.bytes,
            imageContentType = image.contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE,
            imageFilename = image.originalFilename
        )
        exerciseRepository.save(updated)
        model.addAttribute("exercise", updated)
        return "fragments/exercises :: exercise-row"
    }

    @GetMapping("/exercise/{id}/image")
    fun getImage(@PathVariable id: String): ResponseEntity<ByteArray> {
        val existing = exerciseRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Exercise not found") }
        val bytes = existing.image ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, existing.imageContentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .body(bytes)
    }
}