package guru.springframework.spring_6_rest_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundExeption() {
        return ResponseEntity.notFound().build();
    }

}
