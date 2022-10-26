package vttp.csf.Final.Project.controller;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import vttp.csf.Final.Project.dto.AuthenticationResponse;
import vttp.csf.Final.Project.dto.LoginRequest;
import vttp.csf.Final.Project.dto.RefreshTokenRequest;
import vttp.csf.Final.Project.dto.RegisterRequest;
import vttp.csf.Final.Project.repository.UserRepository;
import vttp.csf.Final.Project.service.AuthService;
import vttp.csf.Final.Project.service.RefreshTokenService;

import java.util.logging.Logger;

@RestController
@RequestMapping (path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authSvc;

    @Autowired
    private RefreshTokenService refreshTokenSvc;

    private final Logger logger = Logger.getLogger(AuthController.class.getName());
    
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest regRequest) {
        logger.info("----RegisterRequest: "+regRequest.getEmail()+ regRequest.getUsername()+ regRequest.getPassword());
        authSvc.signup(regRequest);

//        JsonObject resp = Json.createObjectBuilder()
//                .add("message","User registration successful")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(resp.toString());
        return ResponseEntity.status(HttpStatus.OK).body("User registration successful");
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authSvc.verifyAccount(token);

        return ResponseEntity.status(HttpStatus.OK).body("Account activated successfully");
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authSvc.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authSvc.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenSvc.deleteByToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token deleted successfully");
    }
}
