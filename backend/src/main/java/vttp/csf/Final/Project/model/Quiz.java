package vttp.csf.Final.Project.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    private String question;
    private List<String> optionsArray;
    List<Boolean> answerArray;
    private String explanation;
    private String category;

    private static String [] optionsArr = {"a","b", "c", "d", "e"};

    public static List<Quiz> create (String json, String category) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = mapper.readTree(json);

        List<Quiz> quizList = new ArrayList<>();
        if (arrayNode.isArray()) {
            for (JsonNode v : arrayNode) {
                try {
                    Quiz quiz = new Quiz();
                    quiz.setCategory(category);
                    quiz.setQuestion(v.get("question").toString());
                    JsonNode answers = v.get("answers");

                    List<String> options = new ArrayList<>();
                    List<Boolean> answerArr = new ArrayList<>();
                    for (String opt : optionsArr) {
                        if (!answers.get("answer_"+opt).toString().equalsIgnoreCase( "null")) {
                                options.add(answers.get("answer_"+opt).toString());
                        }
                    }
                    quiz.setOptionsArray(options);
                    JsonNode correctAnswers = v.get("correct_answers");
                    for (int i =0; i< options.size(); i++ ){
                        String answer = correctAnswers.get("answer_"+ optionsArr[i] + "_correct").toString();
                        String answerNoQuote = answer.replaceAll("\"", "");

                        answerArr.add(Boolean.valueOf(answerNoQuote));
                    }
                    quiz.setAnswerArray(answerArr);
                    if (v.get("explanation") == null) {
                        quiz.setExplanation("nil");
                    } else {
                        quiz.setExplanation(v.get("explanation").toString());
                    }
                    quizList.add(quiz);
                } catch (Exception e) {
                    throw new RuntimeException("\">>>> QuizService - getQuiz: Error creating List<Quiz>\" +", e);
                }
            }
        }

        return quizList;
    }

}
