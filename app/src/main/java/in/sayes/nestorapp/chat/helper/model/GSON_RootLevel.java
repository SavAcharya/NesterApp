package in.sayes.nestorapp.chat.helper.model;

import java.util.List;

/**
 * Created by sourav on 03/05/16.
 * Project : NesterApp , Package Name : in.sayes.nestorapp.gson
 * Copyright : Sourav Bhattacharya eMail: sav.accharya@gmail.com
 */
public class GSON_RootLevel {

    /**
     * next_endpoint : /user_profiling
     * questions : [{"input_form":{"type":"numeric_keyboard"},"keywords":{},"name_var":"age","options":[],"select_type":"single","statements":["What is your age?"]},{"input_form":{"type":"numeric_keyboard"},"keywords":{},"name_var":"birth_year","options":[],"select_type":"single","statements":["what is your birth year?"]},{"followup":[{"input_form":{"type":"numeric_keyboard"},"name_var":"num_daughters","options":[],"select_type":"single","statements":["Number of Daughters?"]},{}],"input_form":{"type":"floating"},"keywords":{"children":2,"daughter":3,"kids":2},"name_var":"marital_status","options":["yes","no"],"select_type":"single","statements":["Are you married?"]}]
     * user_id : 9972210077
     */

    private String next_endpoint;
    private String user_id;
    /**
     * input_form : {"type":"numeric_keyboard"}
     * keywords : {}
     * name_var : age
     * options : []
     * select_type : single
     * statements : ["What is your age?"]
     */
    private List<String> statements;
    private List<QuestionsEntity> questions;

    public String getNext_endpoint() {
        return next_endpoint;
    }

    public void setNext_endpoint(String next_endpoint) {
        this.next_endpoint = next_endpoint;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<QuestionsEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsEntity> questions) {
        this.questions = questions;
    }

    public static class QuestionsEntity {
        /**
         * type : numeric_keyboard
         */

        private InputFormEntity input_form;
        private String name_var;
        private String select_type;
        private List<OptionType> options;

        private List<String> statements;

        public InputFormEntity getInput_form() {
            return input_form;
        }

        public void setInput_form(InputFormEntity input_form) {
            this.input_form = input_form;
        }

        public String getName_var() {
            return name_var;
        }

        public void setName_var(String name_var) {
            this.name_var = name_var;
        }

        public String getSelect_type() {
            return select_type;
        }

        public void setSelect_type(String select_type) {
            this.select_type = select_type;
        }

        public List<OptionType> getOptions() {
            return options;
        }

        public void setOptions(List<OptionType> options) {
            this.options = options;
        }

        public List<String> getStatements() {
            return statements;
        }

        public void setStatements(List<String> statements) {
            this.statements = statements;
        }


    }

    public static class InputFormEntity {
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class OptionType {
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<Followup> getFollowup() {
            return followup;
        }

        public void setFollowup(List<Followup> followup) {
            this.followup = followup;
        }

        private String value;
        private List<Followup> followup;

    }

    private static class Followup {
        public List<String> getStatements() {
            return statements;
        }

        public void setStatements(List<String> statements) {
            this.statements = statements;
        }

        public List<OptionType> getOptions() {
            return options;
        }

        public void setOptions(List<OptionType> options) {
            this.options = options;
        }

        public InputFormEntity getInput_form() {
            return input_form;
        }

        public void setInput_form(InputFormEntity input_form) {
            this.input_form = input_form;
        }

        public String getName_var() {
            return name_var;
        }

        public void setName_var(String name_var) {
            this.name_var = name_var;
        }

        public String getSelect_type() {
            return select_type;
        }

        public void setSelect_type(String select_type) {
            this.select_type = select_type;
        }

        private List<String> statements;
        private List<OptionType> options;
        private InputFormEntity input_form;
        private String name_var;
        private String select_type;

    }
}
