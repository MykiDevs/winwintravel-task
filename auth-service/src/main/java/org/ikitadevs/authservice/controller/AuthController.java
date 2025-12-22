package org.ikitadevs.authservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.authservice.dto.request.UserDto;
import org.ikitadevs.authservice.repository.UserRepository;
import org.ikitadevs.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserDto userCreateDto) throws ResponseStatusException {
        userService.checkIfExistsByEmail(userCreateDto.getEmail());
        userService.saveUser(userCreateDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid UserDto userLoginDto) throws ResponseStatusException {
        String token = userService.loginUser(userLoginDto);
        return ResponseEntity.ok(token);
    }

}
