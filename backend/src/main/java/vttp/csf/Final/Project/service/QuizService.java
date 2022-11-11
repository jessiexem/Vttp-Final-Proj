package vttp.csf.Final.Project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vttp.csf.Final.Project.model.Quiz;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class QuizService {

    @Value("${QUIZ_API_KEY}")
    private String apiKey;

    private static final Logger logger = Logger.getLogger(QuizService.class.getName());

    private static final String QUIZ_URL
            = "https://quizapi.io/api/v1/questions";

    public Optional<List<Quiz>> getQuiz(String category, String difficulty) {

        String url = UriComponentsBuilder.fromUriString(QUIZ_URL)
                .queryParam("apiKey", apiKey)
                .queryParam("limit", 10)
                .queryParam("tags", category)
                .queryParam("difficulty", difficulty)
                .toUriString();

        logger.info(url);

        RequestEntity req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            logger.info(">>>>QuizService getQuiz: "+resp.getBody());
            List<Quiz> quizList = Quiz.create(resp.getBody(), category);
            return Optional.of(quizList);

        } catch (Exception e) {
            logger.severe(">>>> QuizService - getQuiz: Error creating List<Quiz>");
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
