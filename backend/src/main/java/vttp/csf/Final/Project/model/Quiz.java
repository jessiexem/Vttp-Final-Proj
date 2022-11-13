package vttp.csf.Final.Project.model;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    private String question;
    private List<String> optionsArray;
    List<String> answerArray;
    private String explanation;
    private String category;

    private static String [] optionsArr = {"a","b", "c", "d", "e"};

    public static List<Quiz> create (String json, String category) {
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonArray data = reader.readArray();

        List<Quiz> quizList = new ArrayList<>();
        System.out.println(data);
        data.stream().map(v -> (JsonObject) v)
                .forEach(v-> {
                    try {
                        Quiz quiz = new Quiz();
                        quiz.setCategory(category);
                        quiz.setQuestion(v.getString("question"));
                        JsonObject answers = v.getJsonObject("answers");

                        List<String> options = new ArrayList<>();
                        List<String> answerArr = new ArrayList<>();
                        for (String opt : optionsArr) {
                            if (!answers.isNull("answer_"+opt)) {
                                options.add(answers.getString("answer_"+opt));
                            }
                        }
                        quiz.setOptionsArray(options);
                        JsonObject correctAnswers = v.getJsonObject("correct_answers");
                        for (int i =0; i< options.size(); i++ ){
                            answerArr.add(correctAnswers.getString("answer_"+ optionsArr[i] + "_correct"));
                        }
                        quiz.setAnswerArray(answerArr);
                        if (v.isNull("explanation")) {
                            quiz.setExplanation("nil");
                        } else {
                            quiz.setExplanation(v.getJsonString("explanation").toString());
                        }
                        quizList.add(quiz);
                    } catch (Exception e) {
                        throw new RuntimeException("\">>>> QuizService - getQuiz: Error creating List<Quiz>\" +", e);
                    }
                });

        return quizList;
    }

}
