package com.academia.app.controladores;

import com.academia.app.entidades.Usuario;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import com.academia.app.utils.PassUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
public class AuthController {

    private UsuariosRepo usuariosRepo;

    @Autowired
    public AuthController(UsuariosRepo usuariosRepo) {
        this.usuariosRepo = usuariosRepo;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        Optional<Usuario> usuario = usuariosRepo.findByEmail(email);
        if (usuario.isPresent()) {
            if (PassUtils.check(password, usuario.get().getContraseña())) {
                session.setAttribute("USUARIO", usuario.get().getId());
                return new RedirectView("/");
            } else {
                model.addAttribute("error", "La contraseña es incorrecta");
            }
        } else {
            model.addAttribute("error", "El usuario no existe");
        }
        model.addAttribute("email", email);
        return null;
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/login");
    }

}
