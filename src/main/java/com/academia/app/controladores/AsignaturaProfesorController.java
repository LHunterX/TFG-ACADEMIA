package com.academia.app.controladores;

import com.academia.app.entidades.AsignaturaAlumno;
import com.academia.app.entidades.AsignaturaPromocion;
import com.academia.app.repos.AsignaturasPromocionRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AsignaturaProfesorController {

    private UsuariosRepo usuariosRepo;
    private AsignaturasPromocionRepo asignaturasPromocionRepo;

    @Autowired
    public AsignaturaProfesorController(UsuariosRepo usuariosRepo, AsignaturasPromocionRepo asignaturasPromocionRepo) {
        this.usuariosRepo = usuariosRepo;
        this.asignaturasPromocionRepo = asignaturasPromocionRepo;
    }

    @GetMapping("/asignatura_profesor/{id}")
    public String getAsignatura(Model model, HttpSession session, @PathVariable Long id) {
        Long profesorId = (Long) session.getAttribute("USUARIO");

        model.addAttribute("asignatura", asignaturasPromocionRepo.findById(id).get());
        ModelUtils.setUserData(model, usuariosRepo.findById(profesorId).get());
        return "asignatura_profesor";
    }

    @PostMapping("/asignatura_profesor")
    public String postAsignatura(Model model, HttpSession session, @ModelAttribute("asignatura") AsignaturaPromocion asignaturaPromocion, RedirectAttributes redirAttrs) {
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());

        AsignaturaPromocion current = asignaturasPromocionRepo.findById(asignaturaPromocion.getId()).get();

        for (AsignaturaAlumno alumno : asignaturaPromocion.getAlumnos()) {
            AsignaturaAlumno currentAlumno = current.getAlumnos().stream().filter(a -> a.getId().equals(alumno.getId())).findFirst().get();
        }

        asignaturasPromocionRepo.save(current);
        redirAttrs.addFlashAttribute("mensaje", "Asignatura guardada");
        return "redirect:/historial";
    }

}
