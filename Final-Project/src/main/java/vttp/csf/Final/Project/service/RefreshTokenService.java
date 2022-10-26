package vttp.csf.Final.Project.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.model.RefreshToken;
import vttp.csf.Final.Project.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepo;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString().substring(0,15));
        //refreshToken.setCreatedDate(Instant.now());

        Optional<RefreshToken> opt = refreshTokenRepo.saveToken(refreshToken);
        if (opt.isEmpty()) {
            throw new HomeworkNerdException("Invalid refresh token");
        } else return opt.get();
    }

    void validateRefreshToken(String token) {
        Optional<RefreshToken> opt = refreshTokenRepo.findByToken(token);
        if (opt.isEmpty()) {
            throw new HomeworkNerdException("Invalid refresh token");
        }
    }

    public void deleteByToken(String token) {
        refreshTokenRepo.deleteToken(token);
    }
}
