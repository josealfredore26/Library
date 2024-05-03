package com.example.library.error;

import com.example.library.model.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @RequestMapping("*")
    public ResponseEntity<?> handleDefault() {
        return new ResponseEntity<>(new MessageResponse("Path not found."), HttpStatus.NOT_FOUND);
    }
}