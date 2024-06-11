package com.academia.app.controladores;

import com.academia.app.entidades.Archivo;
import com.academia.app.entidades.Usuario;
import com.academia.app.repos.ArchivosRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import com.academia.app.utils.PassUtils;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;

@Controller
public class PerfilController {

    private UsuariosRepo usuariosRepo;
    private ArchivosRepo archivosRepo;

    @Autowired
    public PerfilController(UsuariosRepo usuariosRepo, ArchivosRepo archivosRepo) {
        this.usuariosRepo = usuariosRepo;
        this.archivosRepo = archivosRepo;
    }

    @GetMapping("/perfil")
    public String getPerfil(Model model, HttpSession session) {
        Usuario usuario = usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get();
        model.addAttribute("usuario", usuario);
        ModelUtils.setUserData(model, usuario);
        return "perfil";
    }

    @PostMapping("/perfil")
    public String guardarPerfil(@ModelAttribute("usuario") Usuario usuario, @RequestParam("foto_file") MultipartFile foto,
                                Model model, RedirectAttributes redirAttrs, HttpSession session) throws IOException {
        byte[] bytes = IOUtils.toByteArray(foto.getInputStream());
        String content = Base64.getEncoder().encodeToString(bytes);

        Archivo archivo = new Archivo(foto.getOriginalFilename(), content);
        archivosRepo.save(archivo);
        usuario.setFoto(archivo);

        Usuario current = usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get();
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            usuario.setContraseña(PassUtils.hash(usuario.getContraseña()));
        } else {
            usuario.setContraseña(current.getContraseña());
        }
        usuario.setRol(current.getRol());
        usuariosRepo.save(usuario);
        redirAttrs.addFlashAttribute("mensaje", "Usuario guardado");
        return "redirect:/perfil";
    }

}
