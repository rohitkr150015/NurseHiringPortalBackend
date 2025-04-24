package com.nuhi.Nuhi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;


    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final String jwt=authHeader.substring(7);
            final String userName=jwtTokenUtil.getUsernameFromToken(jwt);

            if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails =this.userDetailsService.loadUserByUsername(userName);

                if(jwtTokenUtil.validateToken(jwt,userDetails)){

                    UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }
        }
        catch(Exception e){
            logger.error("Cannot set user authentication", e);
        }
      filterChain.doFilter(request, response);
    }
}
