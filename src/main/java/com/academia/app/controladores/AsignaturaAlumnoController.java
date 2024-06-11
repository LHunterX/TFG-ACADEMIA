package com.academia.app.controladores;

import com.academia.app.entidades.*;
import com.academia.app.repos.*;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Controller
public class AsignaturaAlumnoController {

    private UsuariosRepo usuariosRepo;
    private AsignaturasPromocionRepo asignaturasPromocionRepo;
    private ArchivosRepo archivosRepo;
    private EntregasRepo entregasRepo;
    private TareasRepo tareasRepo;

    @Autowired
    public AsignaturaAlumnoController(UsuariosRepo usuariosRepo, AsignaturasPromocionRepo asignaturasPromocionRepo, EntregasRepo entregasRepo, TareasRepo tareasRepo, ArchivosRepo archivosRepo) {
        this.usuariosRepo = usuariosRepo;
        this.asignaturasPromocionRepo = asignaturasPromocionRepo;
        this.entregasRepo = entregasRepo;
        this.archivosRepo = archivosRepo;
        this.tareasRepo = tareasRepo;
    }

    @GetMapping("/asignatura_alumno/{id}")
    public String getAsignatura(Model model, HttpSession session, @PathVariable Long id) {
        Long alumnoId = (Long) session.getAttribute("USUARIO");

        AsignaturaPromocion asignatura = asignaturasPromocionRepo.findById(id).get();

        asignatura.getTareas().forEach(tarea -> {
            tarea.setEntregas(Collections.emptyList());
            Entrega entrega = entregasRepo.findByTareaIdAndAlumnoId(tarea.getId(), alumnoId);
            if (entrega != null) {
                tarea.setEntregas(Collections.singletonList(entrega));
            }
        });

        model.addAttribute("asignatura", asignatura);
        ModelUtils.setUserData(model, usuariosRepo.findById(alumnoId).get());
        return "asignatura_alumno";
    }

    @PostMapping("/entrega")
    public String subirEntrega(@RequestParam("asignatura_id") Long aid, @RequestParam("tarea_id") Long tid,
                               @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Long userId = (Long) session.getAttribute("USUARIO");

        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        String content = Base64.getEncoder().encodeToString(bytes);

        Archivo archivo = new Archivo(file.getOriginalFilename(), content);
        archivosRepo.save(archivo);

        Entrega entrega = entregasRepo.findByTareaIdAndAlumnoId(tid, userId);
        if (entrega != null) {
            archivosRepo.delete(entrega.getArchivo());
            entrega.setArchivo(archivo);
        } else {
            entrega = new Entrega(tareasRepo.findById(tid).get(), usuariosRepo.findById(userId).get(), null, archivo);
        }

        entregasRepo.save(entrega);

        return "redirect:/asignatura_alumno/" + aid;
    }

}
