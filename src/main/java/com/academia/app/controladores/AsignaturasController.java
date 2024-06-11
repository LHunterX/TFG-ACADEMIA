package com.academia.app.controladores;

import com.academia.app.entidades.Asignatura;
import com.academia.app.repos.AsignaturasRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AsignaturasController {

    private UsuariosRepo usuariosRepo;
    private AsignaturasRepo asignaturasRepo;

    @Autowired
    public AsignaturasController(UsuariosRepo usuariosRepo, AsignaturasRepo asignaturasRepo) {
        this.usuariosRepo = usuariosRepo;
        this.asignaturasRepo = asignaturasRepo;
    }

    @GetMapping("/asignaturas")
    public String getAsignaturas(Model model, HttpSession session) {
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        model.addAttribute("asignaturas", asignaturasRepo.findAll());
        model.addAttribute("empty", asignaturasRepo.count() == 0);
        return "asignaturas";
    }

    @GetMapping({"/asignatura/", "/asignatura/{id}"})
    public String getAsignatura(@PathVariable(required = false) String id, Model model, HttpSession session) {
        if (id != null) {
            try {
                Long lid = Long.parseLong(id);
                model.addAttribute("asignatura", asignaturasRepo.findById(lid).get());
            } catch (Exception e) {
                return "redirect:/asignaturas";
            }
        } else {
            model.addAttribute("asignatura", new Asignatura("", ""));
        }
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        return "asignatura";
    }

    @PostMapping("/asignatura")
    public String guardarAsignatura(@ModelAttribute("asignatura") Asignatura asignatura, Model model, RedirectAttributes redirAttrs) {
        asignaturasRepo.save(asignatura);
        redirAttrs.addFlashAttribute("mensaje", "Asignatura guardada");
        return "redirect:/asignaturas";
    }

    @DeleteMapping("/asignatura/{id}")
    public String borrarAsignatura(@PathVariable String id, RedirectAttributes redirAttrs) {
        try {
            Long lid = Long.parseLong(id);
            asignaturasRepo.deleteById(lid);
            redirAttrs.addFlashAttribute("mensaje", "Asignatura eliminada");
        } catch (Exception e) {
        }
        return "redirect:/asignaturas";
    }
}
