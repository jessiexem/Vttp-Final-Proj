package vttp.csf.Final.Project.service;

import java.time.Instant;
import java.util.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import vttp.csf.Final.Project.dto.AuthenticationResponse;
import vttp.csf.Final.Project.dto.LoginRequest;
import vttp.csf.Final.Project.dto.RefreshTokenRequest;
import vttp.csf.Final.Project.dto.RegisterRequest;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.model.NotificationEmail;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.model.VerificationToken;
import vttp.csf.Final.Project.repository.UserRepository;
import vttp.csf.Final.Project.repository.VerificationTokenRepository;
import vttp.csf.Final.Project.security.JwtProvider;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenSvc;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private AmazonS3 s3;

    @Transactional
    public void signup(RegisterRequest regRequest) {
        User user = new User();
        user.setUsername(regRequest.getUsername());
        user.setEmail(regRequest.getEmail());
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));
        user.setEnabled(false);

        Integer userId = userRepo.saveUser(user);
        user.setUserId(userId);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail(
            "Please activate your Account",
            user.getEmail(),
                "Thank you for signing up to HomeworkNerd. Please click on the url to activate your account: "+
                        "https://askit.azurewebsites.net/api/auth/accountVerification/"+ token ));
//            "Thank you for signing up to HomeworkNerd. Please click on the url to activate your account: "+
//            "http://localhost:8080/api/auth/accountVerification/"+ token ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString().substring(0,8);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepo.saveToken(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> opt = verificationTokenRepo.findByToken(token);

        if (opt.isEmpty()) {
            throw new HomeworkNerdException("AuthSvc verifyAccount: Invalid token");
        }

        else fetchUserAndEnable(opt.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        User user = verificationToken.getUser();
        user.setEnabled(true);
        boolean updated = userRepo.updateUser(user);
//        return userRepo.updateUser(user);
        if (!updated) {
            throw new HomeworkNerdException("fetchUserAndEnable -- Unable to enable user");
        }
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        //store authentication obj in security context --> look for Authentication Obj, if present, means user is logged in.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = jwtProvider.generateToken(authentication);
        System.out.println(">>>"+authToken);
        
        Optional<User> optUser = userRepo.findUserByUsername(loginRequest.getUsername());
        String userDpImageUrl = null;
        if (optUser.isPresent()) {
            User user = optUser.get();
            userDpImageUrl = user.getDpImageUrl();
            //String userDpImageUrl = userRepo.getExistingDpImageUrl();
        }

        return AuthenticationResponse.builder()
                .authenticationToken(authToken)
                .refreshToken(refreshTokenSvc.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .dpUrl(userDpImageUrl)
                .build();
    }

    public User getCurrentUser() {
        Authentication loggedInUser = SecurityContextHolder
                .getContext().getAuthentication();

        String username = loggedInUser.getName();

        Optional<User> optUser = userRepo.findUserByUsername(username);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("Username not found :" + username);
        }
        else return optUser.get();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenSvc.validateRefreshToken(refreshTokenRequest.getRefreshToken()); //validate refresh token
        //generate new token
        String authToken = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(authToken)
                .refreshToken(refreshTokenSvc.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public String uploadProfilePicToS3(MultipartFile file) {
        Map<String,String> myData = new HashMap<>();
        myData.put("createdOn", new Date().toString());

        //Metadata for the object
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(myData);

        String hash = UUID.randomUUID().toString().substring(0,8);

        try {
            PutObjectRequest putReq = new PutObjectRequest("jgclass",
                    "display-pic/%s".formatted(hash), file.getInputStream(), metadata);

            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult result = s3.putObject(putReq);
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFileFromS3(final String fileName) {
        s3.deleteObject("jgclass","display-pic/%s".formatted(fileName));
    }

    public boolean updateProfilePic(String fileHash) {
        User user = getCurrentUser();
        String existingDpImageUrl = userRepo.getExistingDpImageUrl(user.getUserId());
        boolean isProfilePicUpdated = userRepo.updateProfilePic(fileHash, user.getUserId());

        if(isProfilePicUpdated) {
            deleteFileFromS3(existingDpImageUrl);
            return true;
        } else {
            return false;
        }
    }
}
