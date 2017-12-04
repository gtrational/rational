package edu.gatech.cs2340.gtrational.rational;

import org.junit.Test;

import edu.gatech.cs2340.gtrational.rational.controller.RegisterActivity;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegisterUnitTest {
    /**
     * Tests addition
     * @throws Exception The exception
     */
    @Test
    public void verify_register_correctly() throws Exception {
        RegisterActivity act = new RegisterActivity();

        String[] messages = {
                "Please enter username",
                "Please enter password",
                "Please confirm password"
        };

        String[] fields = {
                "username",
                "password",
                "password"
        };
        String answer = act.checkFields(fields, messages);
        String correct_answer = "Success";

        assertEquals(correct_answer, answer);

        fields[2] = "password2";

        answer = RegisterActivity.verifyRegister(fields, messages);
        correct_answer = null;

        assertEquals(correct_answer, answer);

        fields[0] = "";
        answer = act.checkFields(fields, messages);
        correct_answer = messages[0];
        assertEquals(correct_answer, answer);

        fields[0] = "username";
        fields[1] = "";
        answer = act.checkFields(fields, messages);
        correct_answer = messages[1];
        assertEquals(correct_answer, answer);

        fields[1] = "password";
        fields[2] = "";
        answer = act.checkFields(fields, messages);
        correct_answer = messages[2];
        assertEquals(correct_answer, answer);

    }
}