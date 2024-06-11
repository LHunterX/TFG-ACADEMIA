package com.academia.app.controladores;

import com.academia.app.entidades.*;
import com.academia.app.enums.Rol;
import com.academia.app.repos.*;
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
public class PromocionesController {

    private UsuariosRepo usuariosRepo;
    private PromocionesRepo promocionesRepo;
    private CursosRepo cursosRepo;
    private AsignaturasRepo asignaturasRepo;
    private AsignaturasPromocionRepo asignaturasPromocionRepo;
    private AsignaturasAlumnoRepo asignaturasAlumnoRepo;

    @Autowired
    public PromocionesController(UsuariosRepo usuariosRepo, PromocionesRepo promocionesRepo, CursosRepo cursosRepo,
                                 AsignaturasRepo asignaturasRepo, AsignaturasAlumnoRepo asignaturasAlumnoRepo, AsignaturasPromocionRepo asignaturasPromocionRepo) {
        this.usuariosRepo = usuariosRepo;
        this.promocionesRepo = promocionesRepo;
        this.cursosRepo = cursosRepo;
        this.asignaturasRepo = asignaturasRepo;
        this.asignaturasAlumnoRepo = asignaturasAlumnoRepo;
        this.asignaturasPromocionRepo = asignaturasPromocionRepo;
    }

    @GetMapping("/promociones")
    public String getPromociones(Model model, HttpSession session) {
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        model.addAttribute("promociones", promocionesRepo.findAll());
        model.addAttribute("empty", promocionesRepo.count() == 0);
        return "promociones";
    }

    @GetMapping({"/promocion/", "/promocion/{id}"})
    public String getPromocion(@RequestParam(name = "curso", required = false, defaultValue = "") String cursoId,
                               @PathVariable(required = false) String id, Model model, HttpSession session) {
        model.addAttribute("todosCursos", cursosRepo.findAll());
        model.addAttribute("profesores", usuariosRepo.findByRol(Rol.PROFESOR));
        model.addAttribute("alumnos", usuariosRepo.findByRol(Rol.ALUMNO));

        Promocion promocion = null;
        Curso curso = null;

        if (id != null) {
            try {
                promocion = promocionesRepo.findById(Long.parseLong(id)).get();
                curso = promocion.getCurso();
            } catch (Exception e) {
                return "redirect:/promociones";
            }
        } else {
            promocion = new Promocion(new Curso(), null, null);
            if (cursoId != null && !cursoId.isEmpty()) {
                try {
                    curso = cursosRepo.findById(Long.parseLong(cursoId)).get();
                } catch (Exception ignore) {
                }
            } else {
                curso = cursosRepo.findFirstByOrderByIdAsc().orElse(new Curso(null, null, new ArrayList<>()));
            }
            List<AsignaturaPromocion> asignaturas = new ArrayList<>();
            for (Asignatura asignatura : curso.getAsignaturas()) {
                AsignaturaPromocion asignaturaP = new AsignaturaPromocion(asignatura, promocion, new Usuario(), new ArrayList<>());
                List<AsignaturaAlumno> alumnos = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    alumnos.add(new AsignaturaAlumno(asignaturaP, usuariosRepo.findByRol(Rol.ALUMNO).iterator().next()));
                }
                asignaturaP.setAlumnos(alumnos);
                asignaturas.add(asignaturaP);
            }
            promocion.setAsignaturas(asignaturas);
        }
        promocion.setCurso(curso);

        model.addAttribute("promocion", promocion);
        model.addAttribute("curso", curso);
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        return "promocion";
    }

    @PostMapping("/promocion")
    public String guardarPromocion(@ModelAttribute("promocion") Promocion promocion, Model model, RedirectAttributes redirAttrs) {
        if (promocion.getId() != null) {
            Promocion current = this.promocionesRepo.findById(promocion.getId()).get();
            promocion.setCurso(current.getCurso());
            promocion.setInicio(current.getInicio());
            promocion.setFin(current.getFin());
        }
        for (AsignaturaPromocion asignaturaPromocion : promocion.getAsignaturas()) {
            asignaturaPromocion.setProfesor(usuariosRepo.findById(asignaturaPromocion.getProfesor().getId()).get());
            asignaturaPromocion.setPromocion(promocion);
            asignaturaPromocion.setAsignatura(asignaturasRepo.findById(asignaturaPromocion.getAsignatura().getId()).get());
            for (AsignaturaAlumno asignaturaAlumno : asignaturaPromocion.getAlumnos()) {
                asignaturaAlumno.setAsignatura(asignaturaPromocion);
                asignaturaAlumno.setAlumno(usuariosRepo.findById(asignaturaAlumno.getAlumno().getId()).get());
            }
        }
        promocionesRepo.save(promocion);
        redirAttrs.addFlashAttribute("mensaje", "Promoción guardada");
        return "redirect:/promociones";
    }

    @DeleteMapping("/promocion/{id}")
    public String borrarPromocion(@PathVariable String id, RedirectAttributes redirAttrs) {
        try {
            Long lid = Long.parseLong(id);
            promocionesRepo.removeById(lid);
            redirAttrs.addFlashAttribute("mensaje", "Promoción eliminada");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("error", "No se ha podido eliminar la promoción");
        }
        return "redirect:/promociones";
    }
}
