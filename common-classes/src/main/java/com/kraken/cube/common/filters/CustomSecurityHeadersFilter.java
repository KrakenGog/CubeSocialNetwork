package com.kraken.cube.common.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomSecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String username = request.getHeader("X-User-Name");
        String idStr = request.getHeader("X-User-Id");
        String rolesStr = request.getHeader("X-User-Roles");

        if (username != null && idStr != null && rolesStr != null) {
            try{
                Long id = Long.parseLong(idStr);
                List<SimpleGrantedAuthority> roles = Arrays.stream(rolesStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

                SecurityUser user = new SecurityUser(Long.valueOf(id), username);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, roles);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            catch(NumberFormatException e){
                log.info("Incorrect user id format in header User-Id-X");
            }
        }

        filterChain.doFilter(request, response);
    }

}
