package io.cordova.myapp00d753.module;

public class BotMessageModule {

    public enum Type {
        USER,
        BOT,
        TYPING
    }

    private String message;
    private Type type;

    public BotMessageModule(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }
}
