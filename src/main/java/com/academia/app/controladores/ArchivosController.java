package com.academia.app.controladores;

import com.academia.app.entidades.Archivo;
import com.academia.app.repos.ArchivosRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Controller
public class ArchivosController {

    private ArchivosRepo archivosRepo;

    @Autowired
    public ArchivosController(ArchivosRepo archivosRepo) {
        this.archivosRepo = archivosRepo;
    }

    @GetMapping("/archivos/{id}")
    public ResponseEntity<Resource> getArchivo(@PathVariable Long id, HttpServletResponse response) {
        Archivo archivo = archivosRepo.findById(id).get();

        byte[] decoder = Base64.getDecoder().decode(archivo.getContent());
        InputStream is = new ByteArrayInputStream(decoder);
        InputStreamResource resource = new InputStreamResource(is);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        ContentDisposition disposition = ContentDisposition.attachment().filename(archivo.getName()).build();
        headers.setContentDisposition(disposition);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

}
