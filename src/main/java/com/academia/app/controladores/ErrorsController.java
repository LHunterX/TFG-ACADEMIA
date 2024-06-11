package com.academia.app.controladores;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ErrorsController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(ErrorsController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Exception ex) {
        Exception e = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (e != null) {
            logger.error("Error: ", e);
        }
        if (ex != null) {
            logger.error("Error: ", ex);
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String msg = "Error desconocido";

        if (status != null && Integer.valueOf(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            msg = "La página no existe";
        }

        model.addAttribute("msg", msg);
        return "error";
    }

}
