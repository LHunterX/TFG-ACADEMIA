package com.academia.app.controladores;

import com.academia.app.entidades.Conversacion;
import com.academia.app.entidades.Mensaje;
import com.academia.app.entidades.Usuario;
import com.academia.app.enums.Rol;
import com.academia.app.repos.ConversacionesRepo;
import com.academia.app.repos.UsuariosRepo;
import com.academia.app.utils.ModelUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class ConversacionesController {

    UsuariosRepo usuariosRepo;
    ConversacionesRepo conversacionesRepo;

    @Autowired
    public ConversacionesController(UsuariosRepo usuariosRepo, ConversacionesRepo conversacionesRepo) {
        this.usuariosRepo = usuariosRepo;
        this.conversacionesRepo = conversacionesRepo;
    }

    @GetMapping("/conversaciones")
    public String getPerfil(Model model, HttpSession session) {
        Usuario usuario = usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get();
        ModelUtils.setUserData(model, usuario);

        Iterable<Conversacion> conversaciones = conversacionesRepo.findAllByUsuario(usuario.getId());

        model.addAttribute("conversaciones", conversaciones);
        model.addAttribute("empty", conversaciones.spliterator().estimateSize() == 0);
        ModelUtils.setUserData(model, usuario);
        return "conversaciones";
    }

    @GetMapping({"/mensaje", "/mensajes/{id}"})
    public String getConversacion(Model model, HttpSession session, @PathVariable(required = false) Long id) {
        Usuario usuario = usuariosRepo.findById((Long) session.getAttribute("USUARIO")).get();
        ModelUtils.setUserData(model, usuario);

        Conversacion conversacion;
        if (id != null) {
            conversacion = conversacionesRepo.findById(id).get();
            model.addAttribute("conversacion", conversacion);
            conversacion.getMensajes().add(new Mensaje(usuario, conversacion.getUsuario1().getId() == usuario.getId()
                    ? conversacion.getUsuario2() : conversacion.getUsuario1(), "", conversacion));

        } else {
            conversacion = new Conversacion(usuario, null);
            model.addAttribute("todosUsuarios", usuariosRepo.findByRolNot(Rol.ADMIN));
            conversacion.getMensajes().add(new Mensaje(usuario, null, "", conversacion));

        }
        model.addAttribute("conversacionSize", conversacion.getMensajes().size() - 1);
        model.addAttribute("conversacion", conversacion);

        return "conversacion";
    }

    @PostMapping("/conversacion")
    public String crearConversacion(Conversacion conversacion, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        if (conversacion.getId() == null) {
            conversacion.setUsuario1(usuariosRepo.findById(conversacion.getUsuario1().getId()).get());
            conversacion.setUsuario2(usuariosRepo.findById(conversacion.getUsuario2().getId()).get());
            conversacion.getMensajes().get(0).setConversacion(conversacion);
            conversacion.getMensajes().get(0).setReceptor(usuariosRepo.findById(conversacion.getUsuario2().getId()).get());
            conversacion.getMensajes().get(0).setEmisor(usuariosRepo.findById(conversacion.getUsuario1().getId()).get());
            conversacionesRepo.save(conversacion);
        } else {
            Conversacion currentConversacion = conversacionesRepo.findById(conversacion.getId()).get();

            Mensaje mensaje = conversacion.getMensajes().get(conversacion.getMensajes().size() - 1);
            mensaje.setConversacion(currentConversacion);
            currentConversacion.getMensajes().add(mensaje);
            conversacionesRepo.save(currentConversacion);
        }


        return "redirect:mensajes/" + conversacion.getId();
    }


}
