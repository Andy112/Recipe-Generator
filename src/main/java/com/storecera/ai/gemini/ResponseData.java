package com.storecera.ai.gemini;

public class ResponseData {

    private String intent;
    private String from;
    private String to;
    private String msg;

    public ResponseData() {}

    public ResponseData(String intent, String msg) {
        this.intent = intent;
        this.msg = msg;
    }

    public String getIntent() {
        return intent;
    }

    public long getStartDate() {
        try {
//            return DateConverter.convertStringToDate(from);
            return 0;
        }
        catch (Exception ignored) {}
        return 0;
    }

    public long getEndDate() {
        try {
//            return DateConverter.convertStringToDate(to);
            return 0;
        }
        catch (Exception ignored) {}
        return 0;
    }

    public String getMsg() {
        return msg != null ? msg : intent == null ?
                "Hmm, I’m not quite sure what you meant. Want to try rephrasing that?" : "";
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "intent='" + intent + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}