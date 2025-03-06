package com.example.dynamicapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token == null) {
            System.out.println("Token não encontrado no cabeçalho Authorization");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Token extraído: " + token);

        if (!jwtTokenProvider.validateToken(token)) {
            System.out.println("Token inválido ou expirado");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        System.out.println("Token válido. Usuário extraído: " + username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String role = jwtTokenProvider.extractRoleFromToken(token);

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Contexto de segurança configurado para o usuário: " + username);

        filterChain.doFilter(request, response);
    }

    // Extrai o token do cabeçalho Authorization
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " do início do token
        }
        return null;
    }
}