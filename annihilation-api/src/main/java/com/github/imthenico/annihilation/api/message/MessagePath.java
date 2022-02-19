package com.github.imthenico.annihilation.api.message;

import com.github.imthenico.simplecommons.util.Validate;

public class MessagePath {

    private final String defaultMessage;
    private final String messagePath;

    public MessagePath(String defaultMessage, String messagePath) {
        this.defaultMessage = Validate.defIfNull(defaultMessage, "");
        this.messagePath = messagePath;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getMessagePath() {
        return messagePath;
    }

    public static MessagePath path(String path) {
        return new MessagePath(null, path);
    }

    public static MessagePath message(String message) {
        return new MessagePath(message, null);
    }
}