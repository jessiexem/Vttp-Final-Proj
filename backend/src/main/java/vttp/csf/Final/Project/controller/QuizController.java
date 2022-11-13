package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vttp.csf.Final.Project.dto.QuizRequest;
import vttp.csf.Final.Project.model.Quiz;
import vttp.csf.Final.Project.service.QuizService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    private static final Logger logger = Logger.getLogger(QuizController.class.getName());

    @PostMapping
    public ResponseEntity<List<Quiz>> takeQuiz(@RequestBody QuizRequest quizRequest) {
        logger.info("in takeQuiz controller.");
        Optional<List<Quiz>> opt = quizService.getQuiz(quizRequest.getCategory(), quizRequest.getDifficulty());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
    }
}
