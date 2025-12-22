package org.ikitadevs.authservice.service;

import lombok.RequiredArgsConstructor;
import org.ikitadevs.authservice.dto.request.UserDto;
import org.ikitadevs.authservice.model.User;
import org.ikitadevs.authservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public void checkIfExistsByEmail(String email) {
    if(userRepository.existsByEmail(email)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists!");
    }
    }

    @Transactional
    public void saveUser(UserDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userRepository.save(user);
    }

    public String loginUser(UserDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getEmail(),
                        userLoginDto.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}
