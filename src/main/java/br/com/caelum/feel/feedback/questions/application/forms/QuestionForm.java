package br.com.caelum.feel.feedback.questions.application.forms;

import br.com.caelum.feel.feedback.questions.domain.models.Question;
import br.com.caelum.feel.feedback.questions.domain.models.vo.Affirmation;
import br.com.caelum.feel.feedback.questions.domain.models.vo.QuestionState;

import javax.validation.constraints.NotEmpty;

public class QuestionForm {

    private Long id;

    @NotEmpty
    private String explanation;

    @NotEmpty
    private String statement;

    @NotEmpty
    private String descriptionOfLowerValue;

    @NotEmpty
    private String descriptionOfHighestValue;

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getDescriptionOfLowerValue() {
        return descriptionOfLowerValue;
    }

    public void setDescriptionOfLowerValue(String descriptionOfLowerValue) {
        this.descriptionOfLowerValue = descriptionOfLowerValue;
    }

    public String getDescriptionOfHighestValue() {
        return descriptionOfHighestValue;
    }

    public void setDescriptionOfHighestValue(String descriptionOfHighestValue) {
        this.descriptionOfHighestValue = descriptionOfHighestValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void fromQuestion(Question question){
        id = question.getId();
        explanation = question.getExplanation();
        statement = question.getStatement();
        descriptionOfLowerValue = question.getDescriptionOfLowerValue();
        descriptionOfHighestValue = question.getDescriptionOfHighestValue();
    }

    public Question toQuestion() {
        var affirmation = createAffirmation();
        return new Question(explanation, affirmation, QuestionState.OPEN);
    }

    public Affirmation getAffirmation(){
        return createAffirmation();
    }

    private Affirmation createAffirmation(){
        return new Affirmation(statement, descriptionOfLowerValue, descriptionOfHighestValue);
    }

}
