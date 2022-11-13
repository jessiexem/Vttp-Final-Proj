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
    List<String> answerArray;
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
                    List<String> answerArr = new ArrayList<>();
                    for (String opt : optionsArr) {
                        if (answers.get("answer_"+opt) != null) {
                            options.add(answers.get("answer_"+opt).toString());
                        }
                    }
                    quiz.setOptionsArray(options);
                    JsonNode correctAnswers = v.get("correct_answers");
                    for (int i =0; i< options.size(); i++ ){
                        answerArr.add(correctAnswers.get("answer_"+ optionsArr[i] + "_correct").toString());
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

//    public static List<Quiz> createBackup (String json, String category) {
//        InputStream is = new ByteArrayInputStream(json.getBytes());
//        JsonReader reader = Json.createReader(is);
//        JsonArray data = reader.readArray();
//
//        List<Quiz> quizList = new ArrayList<>();
//        System.out.println(data);
//        data.stream().map(v -> (JsonObject) v)
//                .forEach(v-> {
//                    try {
//                        Quiz quiz = new Quiz();
//                        quiz.setCategory(category);
//                        quiz.setQuestion(v.getString("question"));
//                        JsonObject answers = v.getJsonObject("answers");
//
//                        List<String> options = new ArrayList<>();
//                        List<String> answerArr = new ArrayList<>();
//                        for (String opt : optionsArr) {
//                            if (!answers.isNull("answer_"+opt)) {
//                                options.add(answers.getString("answer_"+opt));
//                            }
//                        }
//                        quiz.setOptionsArray(options);
//                        JsonObject correctAnswers = v.getJsonObject("correct_answers");
//                        for (int i =0; i< options.size(); i++ ){
//                            answerArr.add(correctAnswers.getString("answer_"+ optionsArr[i] + "_correct"));
//                        }
//                        quiz.setAnswerArray(answerArr);
//                        if (v.isNull("explanation")) {
//                            quiz.setExplanation("nil");
//                        } else {
//                            quiz.setExplanation(v.getJsonString("explanation").toString());
//                        }
//                        quizList.add(quiz);
//                    } catch (Exception e) {
//                        throw new RuntimeException("\">>>> QuizService - getQuiz: Error creating List<Quiz>\" +", e);
//                    }
//                });
//
//        return quizList;
//    }

}
