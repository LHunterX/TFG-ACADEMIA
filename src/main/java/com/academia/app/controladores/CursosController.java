package com.academia.app.controladores;

import com.academia.app.entidades.Asignatura;
import com.academia.app.entidades.Curso;
import com.academia.app.repos.AsignaturasRepo;
import com.academia.app.repos.CursosRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CursosController {

    private UsuariosRepo usuariosRepo;
    private CursosRepo cursosRepo;
    private AsignaturasRepo asignaturasRepo;

    @Autowired
    public CursosController(UsuariosRepo usuariosRepo, CursosRepo cursosRepo, AsignaturasRepo asignaturasRepo) {
        this.usuariosRepo = usuariosRepo;
        this.cursosRepo = cursosRepo;
        this.asignaturasRepo = asignaturasRepo;
    }

    @GetMapping("/cursos")
    public String getCursos(Model model, HttpSession session) {
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        model.addAttribute("cursos", cursosRepo.findAll());
        model.addAttribute("empty", cursosRepo.count() == 0);
        return "cursos";
    }

    @GetMapping({"/curso/", "/curso/{id}"})
    public String getCurso(@PathVariable(required = false) String id, Model model, HttpSession session) {
        model.addAttribute("todasAsignaturas", asignaturasRepo.findAll());
        if (id != null) {
            try {
                Long lid = Long.parseLong(id);
                model.addAttribute("curso", cursosRepo.findById(lid).get());
            } catch (Exception e) {
                return "redirect:/cursos";
            }
        } else {
            List<Asignatura> asignaturas = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                asignaturas.add(new Asignatura());
            }
            model.addAttribute("curso", new Curso("", "", asignaturas));
        }
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        return "curso";
    }

    @PostMapping("/curso")
    public String guardarCurso(@ModelAttribute("curso") Curso curso, Model model, RedirectAttributes redirAttrs) {
        cursosRepo.save(curso);
        redirAttrs.addFlashAttribute("mensaje", "Curso guardada");
        return "redirect:/cursos";
    }

    @DeleteMapping("/curso/{id}")
    public String borrarCurso(@PathVariable String id, RedirectAttributes redirAttrs) {
        try {
            Long lid = Long.parseLong(id);
            cursosRepo.deleteById(lid);
            redirAttrs.addFlashAttribute("mensaje", "Curso eliminado");
        } catch (Exception e) {
        }
        return "redirect:/cursos";
    }
}
