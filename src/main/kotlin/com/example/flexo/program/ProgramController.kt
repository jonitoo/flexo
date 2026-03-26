package com.example.flexo.program

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/programs")
class ProgramController(private val programRepository: ProgramRepository) {

    @PostMapping("/add")
    fun addProgram(@ModelAttribute programDTO: ProgramDTO): String {
        val name = programDTO.name ?: throw IllegalArgumentException("Program name cannot be null")
        programRepository.save(Program(name = name, description = programDTO.description))
        return "redirect:/"
    }

    @PostMapping("/select")
    fun selectProgram(@RequestParam programId: String, session: HttpSession): String {
        session.setAttribute("selectedProgramId", programId)
        return "redirect:/"
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteProgram(@PathVariable id: String) {
        programRepository.deleteById(id)
    }

    @GetMapping("/edit-form")
    fun editProgramForm(@RequestParam id: String, model: Model): String {
        programRepository.findById(id).ifPresent { model.addAttribute("program", it) }
        return "fragments/editprogramform :: editprogramform"
    }

    @PostMapping("/edit")
    fun editProgram(@ModelAttribute programDTO: ProgramDTO): String {
        val id = programDTO.id ?: throw IllegalArgumentException("Program ID cannot be null")
        val name = programDTO.name ?: throw IllegalArgumentException("Program name cannot be null")

        programRepository.findById(id).ifPresent {
            programRepository.save(it.copy(name = name, description = programDTO.description))
        }
        return "redirect:/"
    }
}