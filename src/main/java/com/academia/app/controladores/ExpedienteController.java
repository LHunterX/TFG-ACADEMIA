package com.academia.app.controladores;

import com.academia.app.repos.AsignaturasAlumnoRepo;
import com.academia.app.repos.PromocionesRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ExpedienteController {

    private UsuariosRepo usuariosRepo;
    private AsignaturasAlumnoRepo asignaturasAlumnoRepo;
    private PromocionesRepo promocionesRepo;

    @Autowired
    public ExpedienteController(UsuariosRepo usuariosRepo, AsignaturasAlumnoRepo asignaturasAlumnoRepo, PromocionesRepo promocionesRepo) {
        this.usuariosRepo = usuariosRepo;
        this.asignaturasAlumnoRepo = asignaturasAlumnoRepo;
        this.promocionesRepo = promocionesRepo;
    }

    @GetMapping("/expediente")
    public String getMiCurso(Model model, HttpSession session) {
        Long alumnoId = (Long) session.getAttribute("USUARIO");

        model.addAttribute("promociones", promocionesRepo.findByAlumnoId(alumnoId));

        ModelUtils.setUserData(model, usuariosRepo.findById(alumnoId).get());
        return "expediente";
    }

    @GetMapping("/promocion_alumno/{id}")
    public String getMiCurso(Model model, HttpSession session, @PathVariable String id) {
        Long alumnoId = (Long) session.getAttribute("USUARIO");

        model.addAttribute("asignaturas", asignaturasAlumnoRepo.findByAlumnoYPromocion(alumnoId, Long.parseLong(id)));
        model.addAttribute("expediente", true);
        ModelUtils.setUserData(model, usuariosRepo.findById(alumnoId).get());
        return "promocion_alumno";
    }
}
