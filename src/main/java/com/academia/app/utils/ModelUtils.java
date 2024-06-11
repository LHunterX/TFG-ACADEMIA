package com.academia.app.utils;

import com.academia.app.entidades.Usuario;
import org.springframework.ui.Model;

public abstract class ModelUtils {

    private static String[] adminMenu = {"asignaturas", "cursos", "promociones", "usuarios"};
    private static String[] alumnoMenu = {"mi_curso", "expediente", "conversaciones"};
    private static String[] profesorMenu = {"mis_cursos", "historial", "conversaciones"};

    public static void setUserData(Model model, Usuario usuario) {
        model.addAttribute("user_id", usuario.getId());
        model.addAttribute("user_name", usuario.getName());
        model.addAttribute("user_email", usuario.getEmail());
        if (usuario.getFoto() != null) {
            model.addAttribute("user_foto", usuario.getFoto().getId());
        }

        switch (usuario.getRol()) {
            case ADMIN:
                model.addAttribute("menu", adminMenu);
                break;
            case ALUMNO:
                model.addAttribute("menu", alumnoMenu);
                break;
            case PROFESOR:
                model.addAttribute("menu", profesorMenu);
                break;
        }
    }

}
