package com.academia.app.controladores;

import com.academia.app.repos.AsignaturasAlumnoRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CursoAlumnoController {

    private UsuariosRepo usuariosRepo;
    private AsignaturasAlumnoRepo asignaturasAlumnoRepo;

    @Autowired
    public CursoAlumnoController(UsuariosRepo usuariosRepo, AsignaturasAlumnoRepo asignaturasAlumnoRepo) {
        this.usuariosRepo = usuariosRepo;
        this.asignaturasAlumnoRepo = asignaturasAlumnoRepo;
    }

    @GetMapping("/mi_curso")
    public String getMiCurso(Model model, HttpSession session) {
        Long alumnoId = (Long) session.getAttribute("USUARIO");

        model.addAttribute("asignaturas", asignaturasAlumnoRepo.findByAlumnoIdCursoActual(alumnoId));
        ModelUtils.setUserData(model, usuariosRepo.findById(alumnoId).get());
        return "promocion_alumno";
    }

}
