package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;
import vttp.csf.Final.Project.dto.*;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.UserRepository;
import vttp.csf.Final.Project.service.AuthService;
import vttp.csf.Final.Project.service.RefreshTokenService;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping (path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authSvc;

    @Autowired
    private RefreshTokenService refreshTokenSvc;

    @Autowired
    private UserRepository userRepo;

    private final Logger logger = Logger.getLogger(AuthController.class.getName());
    
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest regRequest) {
        logger.info("----RegisterRequest: "+regRequest.getEmail()+ regRequest.getUsername()+ regRequest.getPassword());

        //check if username is taken
        Optional<User> optUser = userRepo.findUserByUsername(regRequest.getUsername());
        if(optUser.isEmpty()) {
            authSvc.signup(regRequest);

            return ResponseEntity.status(HttpStatus.OK).body("User registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken.");
        }

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

    @PostMapping( path = "/dp",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfilePicResponse> uploadProfilePic(@RequestPart MultipartFile file) {
        String fileHash = authSvc.uploadProfilePicToS3(file);

        if(fileHash == null) {
            //Unable to save profile picture in S3
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else {
            boolean isProfilePicUpdated = authSvc.updateProfilePic(fileHash);

            if(isProfilePicUpdated) {
                ProfilePicResponse resp = new ProfilePicResponse();
                resp.setProfilePicUrl(fileHash);

                return ResponseEntity.status(HttpStatus.OK).body(resp);
            } else {
                //Unable to save profile picture in db
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }
}
