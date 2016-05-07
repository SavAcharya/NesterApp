package in.sayes.nestorapp.signup.helper.model;

import java.util.List;

/**
 * Created by sourav on 03/05/16.
 * Project : NesterApp , Package Name : in.sayes.nestorapp.signup.helper.model
 * Copyright : Sourav Bhattacharya eMail: sav.accharya@gmail.com
 */
public class GSON_Signup {

    /**
     * next_endpoint : /root_level
     * questions : [{"input_form":{"type":"cascade_cards"},"name_var":"user_query",
     * "options":["I am new to tax saving investment. Please start from scratch.",
     * "I pay rent. Can you help me with HRA exemption?",
     * "I always feel my in hand salary is way less than my CTC.",
     * "I have done investment in PPF. Can I take benefit?","I have done investment in PF. Can I get benefit?"],
     * "select_type":"single","statements":["This is Nestor!","Welcome, select the option you care about?"]},
     * {"input_form":{"height":1,"type":"custom_keyboard","width":1},"name_var":"user_start",
     * "options":["Lets start to plan it"],"select_type":"single",
     * "statements":["Cool, like everything else this too needs some planning."]}]
     * user_id : 9972210077
     */

    private String next_endpoint;
    private String user_id;
    /**
     * input_form : {"type":"cascade_cards"}
     * name_var : user_query
     * options : ["I am new to tax saving investment. Please start from scratch.",
     * "I pay rent. Can you help me with HRA exemption?","I always feel my in hand salary is way less than my CTC.",
     * "I have done investment in PPF. Can I take benefit?","I have done investment in PF. Can I get benefit?"]
     * select_type : single
     * statements : ["This is Nestor!","Welcome, select the option you care about?"]
     */

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
         * type : cascade_cards
         */

        private InputFormEntity input_form;
        private String name_var;
        private String select_type;
        private List<String> options;
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

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public List<String> getStatements() {
            return statements;
        }

        public void setStatements(List<String> statements) {
            this.statements = statements;
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
    }
}
