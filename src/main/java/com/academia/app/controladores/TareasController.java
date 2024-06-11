package com.academia.app.controladores;

import com.academia.app.entidades.AsignaturaPromocion;
import com.academia.app.entidades.Entrega;
import com.academia.app.entidades.Tarea;
import com.academia.app.repos.ArchivosRepo;
import com.academia.app.repos.TareasRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TareasController {

    private UsuariosRepo usuariosRepo;
    private TareasRepo tareasRepo;
    private ArchivosRepo archivosRepo;

    @Autowired
    public TareasController(UsuariosRepo usuariosRepo, TareasRepo tareasRepo, ArchivosRepo archivosRepo) {
        this.usuariosRepo = usuariosRepo;
        this.tareasRepo = tareasRepo;
        this.archivosRepo = archivosRepo;
    }

    @GetMapping({"/asignatura_profesor/{aid}/tarea", "/asignatura_profesor/{aid}/tarea/{id}"})
    public String getTarea(@PathVariable String aid, @PathVariable(required = false) String id, Model model, HttpSession session) {
        if (id != null) {
            try {
                Long lid = Long.parseLong(id);
                model.addAttribute("tarea", tareasRepo.findById(lid).get());
            } catch (Exception e) {
                return "redirect:/tareas";
            }
        } else {
            model.addAttribute("tarea", new Tarea(new AsignaturaPromocion(Long.parseLong(aid)), "", "", null));
        }
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        return "tarea";
    }

    @PostMapping("/asignatura_profesor/{aid}/tarea")
    public String guardarTarea(@PathVariable String aid, @ModelAttribute("tarea") Tarea tarea, Model model, RedirectAttributes redirAttrs) {
        if (tarea.getEntregas() != null) {
            for (Entrega entrega : tarea.getEntregas()) {
                entrega.setAlumno(usuariosRepo.findById(entrega.getAlumno().getId()).get());
                entrega.setArchivo(archivosRepo.findById(entrega.getArchivo().getId()).get());
                entrega.setTarea(tarea);
            }
        }
        tareasRepo.save(tarea);
        redirAttrs.addFlashAttribute("mensaje", "Tarea guardada");
        return "redirect:/asignatura_profesor/" + aid;
    }

    @DeleteMapping("/tarea/{id}")
    public String borrarTarea(@PathVariable String id, RedirectAttributes redirAttrs) {
        try {
            Long lid = Long.parseLong(id);
            tareasRepo.deleteById(lid);
            redirAttrs.addFlashAttribute("mensaje", "Tarea eliminada");
        } catch (Exception e) {
        }
        return "redirect:/tareas";
    }
}
