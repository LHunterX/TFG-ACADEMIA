package com.academia.app.controladores;

import com.academia.app.entidades.AsignaturaPromocion;
import com.academia.app.entidades.Promocion;
import com.academia.app.repos.AsignaturasPromocionRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
public class CursosProfesorController {

    private UsuariosRepo usuariosRepo;
    private AsignaturasPromocionRepo asignaturasPromocionRepo;

    @Autowired
    public CursosProfesorController(UsuariosRepo usuariosRepo, AsignaturasPromocionRepo asignaturasPromocionRepo) {
        this.usuariosRepo = usuariosRepo;
        this.asignaturasPromocionRepo = asignaturasPromocionRepo;
    }

    @GetMapping("/mis_cursos")
    public String getMiCurso(Model model, HttpSession session) {
        // Se obtienen las asignaturas de la promoción actual del profesor
        Long profesorId = (Long) session.getAttribute("USUARIO");

        Map<Long, Promocion> promociones = new HashMap<>();

        // Se agrupan las asignaturas por promoción
        for (AsignaturaPromocion asignaturaPromocion : asignaturasPromocionRepo.findAllByProfesorIdActuales(profesorId)) {
            if (promociones.get(asignaturaPromocion.getPromocion().getId()) == null) {
                Promocion promocion = new Promocion(asignaturaPromocion.getPromocion().getCurso(), asignaturaPromocion.getPromocion().getInicio(), asignaturaPromocion.getPromocion().getFin());
                promocion.setAsignaturas(new ArrayList<>());
                promociones.put(asignaturaPromocion.getPromocion().getId(), promocion);

            }
            promociones.get(asignaturaPromocion.getPromocion().getId()).getAsignaturas().add(asignaturaPromocion);
        }

        model.addAttribute("promociones", promociones.values());
        model.addAttribute("title", "Mis Cursos");
        ModelUtils.setUserData(model, usuariosRepo.findById(profesorId).get());
        return "promociones_profesor";
    }

    @GetMapping("/historial")
    public String getHistorial(Model model, HttpSession session) {
        Long profesorId = (Long) session.getAttribute("USUARIO");

        Map<Long, Promocion> promociones = new HashMap<>();

        // Se agrupan las asignaturas por promoción
        for (AsignaturaPromocion asignaturaPromocion : asignaturasPromocionRepo.findAllByProfesorId(profesorId)) {
            if (promociones.get(asignaturaPromocion.getPromocion().getId()) == null) {
                Promocion promocion = new Promocion(asignaturaPromocion.getPromocion().getCurso(), asignaturaPromocion.getPromocion().getInicio(), asignaturaPromocion.getPromocion().getFin());
                promocion.setAsignaturas(new ArrayList<>());
                promociones.put(asignaturaPromocion.getPromocion().getId(), promocion);

            }
            promociones.get(asignaturaPromocion.getPromocion().getId()).getAsignaturas().add(asignaturaPromocion);
        }

        model.addAttribute("promociones", promociones.values());
        model.addAttribute("title", "Historial");
        ModelUtils.setUserData(model, usuariosRepo.findById(profesorId).get());
        return "promociones_profesor";
    }

}
