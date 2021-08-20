package digital.patron.common.rest;


import digital.patron.AdminMembers.domain.Admin;
import digital.patron.AdminMembers.repository.AdminRepository;
import digital.patron.common.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private AdminRepository adminRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
         try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Admin doesn't exist"));
            String token = jwtTokenProvider.createToken(request.getEmail(), admin.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("email", request.getEmail());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid /password combination", HttpStatus.FORBIDDEN);
        }
    }
//    public ResponseEntity<?> authenticate(@RequestParam String email, String password) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password ));
//            Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Admin doesn't exist"));
//            String token = jwtTokenProvider.createToken(email, admin.getRole().name());
//            Map<Object, Object> response = new HashMap<>();
//            response.put("email", email);
//            response.put("token", token);
//
//            return ResponseEntity.ok(response);
//        } catch (AuthenticationException e) {
//            return new ResponseEntity<>("Invalid /password combination", HttpStatus.FORBIDDEN);
//        }
//    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
