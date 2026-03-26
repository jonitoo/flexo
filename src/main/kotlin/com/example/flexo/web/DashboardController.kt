package com.example.flexo.web

import com.example.flexo.exercise.ExerciseRepository
import com.example.flexo.program.ProgramRepository
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/")
class DashboardController(
    private val exerciseRepository: ExerciseRepository,
    private val programRepository: ProgramRepository
) {
    private val log = LoggerFactory.getLogger(DashboardController::class.java)

    /**
     * Renders the main dashboard page displaying all exercises.
     */
    @GetMapping("/")
    fun dashboardPage(model: Model, session: HttpSession): String {
        val selectedProgramId = session.getAttribute("selectedProgramId") as? String
        val exercises = selectedProgramId?.let {
            exerciseRepository.findByProgramIdOrderBySortOrderAsc(it)
        } ?: exerciseRepository.findAllByOrderBySortOrderAsc()
        model.addAttribute("exercises", exercises)
        model.addAttribute("programs", programRepository.findAll())
        model.addAttribute("selectedProgramId", selectedProgramId)
        return "dashboard"
    }

    /**
     * Form to edit an existing exercise item.
     */
    @GetMapping("/edit-form")
    fun getFormFields(
        @RequestParam id: String?,
        @RequestParam name: String?,
        @RequestParam description: String?,
        @RequestParam timerDurationSeconds: Int?,
        model: Model
    ): String {
        model.addAllAttributes(
            mapOf(
                "id" to id,
                "name" to name,
                "description" to description,
                "timerDurationSeconds" to (timerDurationSeconds ?: 60)
            )
        )
        return "fragments/core :: editform"
    }

    /**
     * Form to upload an image for the exercise.
     */
    @GetMapping("/exercise/{id}/image/upload-form")
    fun uploadImageForm(@PathVariable id: String, model: Model): String {
        log.info("Serving upload image form for exercise id={}", id)
        model.addAttribute("id", id)
        return "fragments/core :: uploadimageform"
    }
}