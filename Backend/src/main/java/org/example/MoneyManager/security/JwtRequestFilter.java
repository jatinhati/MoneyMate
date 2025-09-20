package org.example.MoneyManager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.MoneyManager.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        System.out.println("JWT Filter processing: " + requestPath);
        
        // Skip JWT processing for public endpoints
        if (isPublicEndpoint(requestPath)) {
            System.out.println("Skipping JWT processing for public endpoint: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            jwt = authHeader.substring(7);
            try {
                email = jwtUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                // Log the error but don't throw it, let the request continue
                System.err.println("JWT processing error: " + e.getMessage());
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if (jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // Log the error but don't throw it, let the request continue
                System.err.println("User authentication error: " + e.getMessage());
            }
        }
        filterChain.doFilter(request,response);
    }
    
    private boolean isPublicEndpoint(String requestPath) {
        return requestPath.endsWith("/register") ||
               requestPath.endsWith("/login") ||
               requestPath.endsWith("/activate") ||
               requestPath.endsWith("/status") ||
               requestPath.startsWith("/test/");
    }
}
