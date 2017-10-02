import beans.YandexPredictorAnswer;
import beans.YandexPredictorError;
import enums.Errors;
import enums.Languages;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import static core.YandexPredictorApi.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class TestYandexPredictor {

    @Test
    public void simpleApiCall(){
        RestAssured
                .given()
                    .queryParams(PARAM_KEY, PREDICTOR_KEY, PARAM_LANG, Languages.EN, PARAM_TEXT, "hello")
                    .accept(ContentType.JSON)
                    .log().everything()
                .when()
                    .get(YANDEX_PREDICTOR_API_URI)
                    .prettyPeek()
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .header("Connection", Matchers.equalTo("keep-alive"));
    }

    @Test
    public void testCorrectOneWordPrediction(){
        YandexPredictorAnswer answer = getYandexPredictorAnswer(
                with().key(PREDICTOR_KEY).language(Languages.RU).text("Кот в сап").callApi());

        assertThat(answer.text.size(), equalTo(1));
        assertThat(answer.text.get(0), equalTo("сапогах"));
        assertThat(answer.endOfWord, equalTo(false));
        assertThat(answer.pos, equalTo(-3));
    }

    @Test
    public void testCorrectTwoWordsPrediction(){
        YandexPredictorAnswer answer = getYandexPredictorAnswer(
                with().key(PREDICTOR_KEY).language(Languages.EN).text("Hello").limit(2).callApi());

        assertThat(answer.text.size(), equalTo(2));
        assertThat(answer.text.get(0), equalTo("kitty"));
        assertThat(answer.text.get(1), equalTo("to"));
        assertThat(answer.endOfWord, equalTo(true));
        assertThat(answer.pos, equalTo(1));
    }

    @Test
    public void testManyWordsPrediction(){
        YandexPredictorAnswer answer = getYandexPredictorAnswer(
                with().key(PREDICTOR_KEY).language(Languages.EN).text("Hi").limit(10).callApi());

        assertThat(answer.text.size(), lessThanOrEqualTo(10));
        assertThat(answer.endOfWord, equalTo(false));
        assertThat(answer.pos, equalTo(-2));
    }

    @Test
    public void testErrorIncorrectLanguage(){
        YandexPredictorError error = getYandexPredictorError(
                with().key(PREDICTOR_KEY).language(Languages.ERR).text("Hello").limit(2).callApi());

        assertThat(error.code, equalTo(Errors.ERR_LANG_NOT_SUPPORTED.errorCode));
        assertThat(error.message, equalTo(Errors.ERR_LANG_NOT_SUPPORTED.message));
    }

    @Test
    public void testTextTooLongError(){
        String text = "";
        for (int i = 0; i < 2000; i++){
            text += "a";
        }

        YandexPredictorError error = getYandexPredictorError(
                with().key(PREDICTOR_KEY).language(Languages.EN).text(text).limit(2).callApi());

        assertThat(error.code, equalTo(Errors.ERR_TEXT_TOO_LONG.errorCode));
        assertThat(error.message, equalTo(Errors.ERR_TEXT_TOO_LONG.message));
    }

    @Test
    public void testNoTextAnswer(){
        YandexPredictorAnswer answer = getYandexPredictorAnswer(
                with().key(PREDICTOR_KEY).language(Languages.EN).callApi());

        assertThat(answer.text.size(), equalTo(0));
        assertThat(answer.endOfWord, equalTo(false));
        assertThat(answer.pos, equalTo(0));
    }

    @Test
    public void testInvalidKeyError(){
        String wrongKey = "wrong key";
        YandexPredictorError error = getYandexPredictorError(
                with().key(wrongKey).language(Languages.EN).text("Hello").callApi());

        assertThat(error.code, equalTo(Errors.ERR_KEY_INVALID.errorCode));
        assertThat(error.message, equalTo(Errors.ERR_KEY_INVALID.message));
    }
}
