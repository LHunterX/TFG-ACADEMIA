package com.academia.app.controladores;

import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private UsuariosRepo usuariosRepo;

    @Autowired
    public HomeController(UsuariosRepo usuariosRepo) {
        this.usuariosRepo = usuariosRepo;
    }

    @GetMapping("/")
    public String getHome(Model model, HttpSession session) {
        ModelUtils.setUserData(model, usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get());
        return "index";
    }

}
