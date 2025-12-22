package org.ikitadevs.authservice.filter;



import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.authservice.dto.response.ErrorResponse;
import org.ikitadevs.authservice.model.User;
import org.ikitadevs.authservice.model.enums.Role;
import org.ikitadevs.authservice.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                User userDetails = new User();
                userDetails.setEmail(jwtService.extractEmail(jwt));
                userDetails.setId(jwtService.extractId(jwt));
                /* If we need to store roles in a separate table
                   We can use @ElementCollection, @CollectionTable to create different table for roles
                */
                Set<Role> roleSet = jwtService.extractRoles(jwt);
                log.info("Roles: {}", roleSet);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() //returns ROLE_USER always (for simplify)
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (ExpiredJwtException e) {
                log.warn("Auth token is expired: {}", e.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error("Token is expired!")
                        .timestamp(Instant.now()).build();
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
                return;
            } catch (MalformedJwtException e) {
                log.warn("Auth token is invalid: {}", e.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error("Token is invalid!")
                        .timestamp(Instant.now()).build();
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
                return;
            }
            filterChain.doFilter(request, response);
        }

    }
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, ErrorResponse errorResponse) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);

    }
}
