package com.example.controller;

import com.example.constant.HttpStatus;
import com.example.domain.ResponseEntity;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotFoundController implements ErrorController {

    /**
     * 由于404异常无法被全局异常处理，可通过此种方式进行处理404异常
     */
    @RequestMapping("/error")
    public ResponseEntity<Void> handleError() {
        return ResponseEntity.fail(HttpStatus.NOT_FOUND, "Not Found Exception");
    }

}

