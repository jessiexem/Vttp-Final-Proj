package vttp.csf.Final.Project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import vttp.csf.Final.Project.repository.UserRepository;
import vttp.csf.Final.Project.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    JwtProvider jwtProvider;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    AuthService authService;
//
//    public JwtFilter(JwtProvider jwtProvider,
//                          UserRepository userRepo) {
//        this.jwtProvider = jwtProvider;
//        this.userRepository = userRepo;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (isEmpty(header) || !header.startsWith("Bearer")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Get jwt token and validate
//        final String token = header.split(" ")[1].trim();
////        if (!jwtProvider.validate(token)) {
//        if (!authService.verifyAccount(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Get user identity and set it on the spring security context
//        UserDetails userDetails = userRepository
//                .findUserByUsername(jwtProvider.getUsername(token))
//                .orElse(null);
////        UserDetails userDetails = userRepository
////                .findUserByUsername(jwtProvider.getUsername(token))
////                .orElse(null);
//
//        UsernamePasswordAuthenticationToken
//                authentication = new UsernamePasswordAuthenticationToken(
//                userDetails, null,
//                userDetails == null ?
//                        List.of() : userDetails.getAuthorities()
//        );
//
//        authentication.setDetails(
//                new WebAuthenticationDetailsSource().buildDetails(request)
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        filterChain.doFilter(request, response);
//    }
//}
