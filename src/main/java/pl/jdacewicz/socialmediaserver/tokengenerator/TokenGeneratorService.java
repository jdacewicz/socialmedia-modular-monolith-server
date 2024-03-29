package pl.jdacewicz.socialmediaserver.tokengenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.jdacewicz.socialmediaserver.userdatareceiver.UserDataReceiverFacade;

import java.util.List;

@Service
@RequiredArgsConstructor
class TokenGeneratorService {

    private final TokenGeneratorRepository tokenGeneratorRepository;
    private final TokenGenerator tokenGenerator;
    private final UserDataReceiverFacade userDataReceiverFacade;

    Token getTokenByCode(String code) {
        return tokenGeneratorRepository.findByCode(code)
                .orElseThrow(UnsupportedOperationException::new);
    }

    Token createToken(String username) {
        var preparedToken = prepareToken(username);
        return tokenGeneratorRepository.save(preparedToken);
    }

    void revokeAllUserTokens(String userId) {
        var userTokens = tokenGeneratorRepository.findByUserId(userId);
        var revokedTokens = revokeTokens(userTokens);
        tokenGeneratorRepository.saveAll(revokedTokens);
    }

    @Scheduled(cron = "${application.scheduled-tasks.delete-all-data.cron}")
    @Profile("demo")
    void deleteAllTokens() {
        tokenGeneratorRepository.deleteAll();
    }

    private Token prepareToken(String username) {
        var userDto = userDataReceiverFacade.getUserByEmail(username);
        var generatedTokenCode = tokenGenerator.generateToken(username);
        return Token.builder()
                .userId(userDto.getUserId())
                .code(generatedTokenCode)
                .expired(false)
                .revoked(false)
                .build();
    }

    private List<Token> revokeTokens(List<Token> userTokens) {
        return userTokens.stream()
                .map(Token::toggleRevoked)
                .toList();
    }
}
