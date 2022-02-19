package com.github.imthenico.annihilation.api.match.authorization;

import com.github.imthenico.annihilation.api.message.MessagePath;
import com.github.imthenico.simplecommons.util.Validate;

public class AuthorizationResult {

    private final boolean authorized;
    private final MessagePath reasonMessage;
    private final String reason;

    public AuthorizationResult(
            boolean authorized,
            MessagePath reasonMessage,
            String reason
    ) {
        this.authorized = authorized;
        this.reasonMessage = reasonMessage;
        this.reason = reason;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public MessagePath getReasonMessage() {
        return reasonMessage;
    }

    public String getReason() {
        return reason;
    }

    public static AuthorizationResult of(boolean success, MessagePath errorMessage, String reason) {
        return new AuthorizationResult(success, errorMessage, reason);
    }
}