package me.rgunny.levelup.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception")
public class ExceptionController {

    @GetMapping()
    public ResponseEntity<String> testController() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("/api/exception SUCCESS");
    }
}
