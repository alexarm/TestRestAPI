package core;

import beans.YandexPredictorAnswer;
import beans.YandexPredictorError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.Languages;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

public class YandexPredictorApi {

    //100 000 requests per day
    //1 000 000 symbols per day

    public static final String YANDEX_PREDICTOR_API_URI =
            "https://predictor.yandex.net/api/v1/predict.json/complete";
    public  static  final String PREDICTOR_KEY =
            "pdct.1.1.20171001T151737Z.6c58496daa7d24f0.5db8da531ccd0a9014b5c39e7eff6aa75acdb9bb";
    public static final String PARAM_LANG = "lang";
    public static final String PARAM_TEXT = "q";
    public static final String PARAM_LIMIT = "limit";
    public static final String PARAM_KEY = "key";

    private HashMap<String, String> params = new HashMap<String, String>();

    public static class ApiBuilder {
        YandexPredictorApi predictorApi;

        private ApiBuilder(YandexPredictorApi gcApi) {
            predictorApi = gcApi;
        }

        public ApiBuilder key(String key) {
            predictorApi.params.put(PARAM_KEY, key);
            return this;
        }

        public ApiBuilder text(String text) {
            predictorApi.params.put(PARAM_TEXT, text);
            return this;
        }

        public ApiBuilder language(Languages language) {
            predictorApi.params.put(PARAM_LANG, language.languageCode);
            return this;
        }

        public ApiBuilder limit(int limit) {
            predictorApi.params.put(PARAM_LIMIT, String.valueOf(limit));
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParams(predictorApi.params)
                    .log().all()
                    .get(YANDEX_PREDICTOR_API_URI).prettyPeek();
        }
    }

    public static ApiBuilder with() {
        YandexPredictorApi api = new YandexPredictorApi();
        return new ApiBuilder(api);
    }

    //get ready Predictor answer form api response
    public static YandexPredictorAnswer getYandexPredictorAnswer(Response response){
        return new Gson().fromJson(response.asString(), new TypeToken<YandexPredictorAnswer>() {}.getType());
    }

    //get ready Predictor error form api response
    public static YandexPredictorError getYandexPredictorError(Response response){
        return new Gson().fromJson(response.asString(), new TypeToken<YandexPredictorError>() {}.getType());
    }
}
