package io.cordova.myapp00d753.module;

import android.graphics.Bitmap;

public class BotMessageModule {

    public enum Type {
        USER,
        BOT,
        TYPING,
        BOT_BUTTON,
        IMAGE
    }

    private String message;
    private Type type;
    private Bitmap image;
    private boolean isOption;
    private String optionText;
    private String optionKey;

    public BotMessageModule(String message, Type type) {
        this.message = message;
        this.type = type;
    }
    public BotMessageModule(Bitmap image) {
        this.image = image;
        this.type = Type.IMAGE;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public Bitmap getImage() {
        return image;
    }

    public BotMessageModule(String optionText, String optionKey) {
        this.isOption = true;
        this.optionText = optionText;
        this.optionKey = optionKey;
    }

    public boolean isOption() { return isOption; }
    public String getOptionText() { return optionText; }
    public String getOptionKey() { return optionKey; }
}
