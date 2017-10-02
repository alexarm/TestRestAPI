package enums;

public enum Errors {
    ERR_OK(200, ""),
    ERR_KEY_INVALID(401, "API key is invalid"),
    ERR_KEY_BLOCKED(402, "API key is blocked"),
    ERR_DAILY_REQ_LIMIT_EXCEEDED(403, ""),
    ERR_DAILY_CHAR_LIMIT_EXCEEDED(404, ""),
    ERR_TEXT_TOO_LONG(413, "The text size exceeds the maximum"),
    ERR_LANG_NOT_SUPPORTED(501, "The specified language is not supported");

    public int errorCode;
    public String message;

    private Errors(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
