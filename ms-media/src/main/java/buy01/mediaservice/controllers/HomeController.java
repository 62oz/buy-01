package buy01.ms-media.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Welcome to buy01 media service :)");
    }
}
