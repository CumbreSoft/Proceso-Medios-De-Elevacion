package com.maticolque.apirestelevadores.controllerrequests;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ErrorController {

    @PostMapping("**") // Captura todas las URL incorrectas GET
    public ResponseEntity<String> handleInvalidPostURL() {
        String mensaje = "La URL ingresada (POST) es incorrecta. Por favor, verifica la ruta.";
        return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
    }


    @GetMapping("**") // Captura todas las URL incorrectas POST
    public ResponseEntity<String> handleInvalidGetURL() {
        String mensaje = "La URL ingresada (GET) es incorrecta. Por favor, verifica la ruta.";
        return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
    }


    @PutMapping("**") // Captura todas las URL incorrectas PUT
    public ResponseEntity<String> handleInvalidGetPutURL() {
        String mensaje = "La URL ingresada (PUT) es incorrecta. Por favor, verifica la ruta.";
        return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("**") // Captura todas las URL incorrectas DELETE
    public ResponseEntity<String> handleInvalidDeletePutURL() {
        String mensaje = "La URL ingresada (DELETE) es incorrecta. Por favor, verifica la ruta.";
        return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
    }
}
