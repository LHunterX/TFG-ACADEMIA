package com.academia.app.controladores;

import com.academia.app.entidades.Usuario;
import com.academia.app.enums.Rol;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import com.academia.app.utils.PassUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuariosController {

    private UsuariosRepo usuariosRepo;

    @Autowired
    public UsuariosController(UsuariosRepo usuariosRepo) {
        this.usuariosRepo = usuariosRepo;
        if (this.usuariosRepo.count() == 0) {
            this.usuariosRepo.save(new Usuario("admin", "admin@mail.com", PassUtils.hash("admin"), Rol.ADMIN));
        }
    }

    @GetMapping("/usuarios")
    public String getUsuarios(Model model, HttpSession session, @RequestParam(value = "rol", required = false) String rol,
                              @RequestParam(value = "search", required = false) String search) {
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        Iterable<Usuario> usuarios;

        if (rol != null && !rol.isEmpty()) {
            if (search != null && !search.isEmpty()) {
                usuarios = usuariosRepo.findByRolAndNameContaining(Rol.values()[Integer.parseInt(rol)], search);
            } else {
                usuarios = usuariosRepo.findByRol(Rol.values()[Integer.parseInt(rol)]);
            }
        } else {
            if (search != null && !search.isEmpty()) {
                usuarios = usuariosRepo.findByNameContaining(search);
            } else {
                usuarios = usuariosRepo.findAll();
            }
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("rol", rol);
        model.addAttribute("search", search != null  ? search : "");
        return "usuarios";
    }

    @GetMapping({"/usuario/", "/usuario/{id}"})
    public String getUsuario(@PathVariable(required = false) String id, Model model, HttpSession session) {
        if (id != null) {
            try {
                Long lid = Long.parseLong(id);
                model.addAttribute("usuario", usuariosRepo.findById(lid).get());
            } catch (Exception e) {
                return "redirect:/usuarios";
            }
        } else {
            model.addAttribute("usuario", new Usuario("", "", "", null));
        }
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        return "usuario";
    }

    @PostMapping("/usuario")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, RedirectAttributes redirAttrs) {
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            usuario.setContraseña(PassUtils.hash(usuario.getContraseña()));
        } else {
            usuario.setContraseña(usuariosRepo.findById(usuario.getId()).get().getContraseña());
        }
        usuariosRepo.save(usuario);
        redirAttrs.addFlashAttribute("mensaje", "Usuario guardado");
        return "redirect:/usuarios";
    }

    @DeleteMapping("/usuario/{id}")
    public String borrarUsuario(@PathVariable String id, RedirectAttributes redirAttrs) {
        try {
            Long lid = Long.parseLong(id);
            usuariosRepo.deleteById(lid);
            redirAttrs.addFlashAttribute("mensaje", "Usuario eliminado");
        } catch (Exception e) {
        }
        return "redirect:/usuarios";
    }
}
