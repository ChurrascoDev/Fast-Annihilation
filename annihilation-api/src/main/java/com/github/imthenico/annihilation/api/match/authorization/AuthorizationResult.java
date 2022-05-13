package com.github.imthenico.annihilation.api.match.authorization;

import com.github.imthenico.annihilation.api.message.MessagePath;
import com.github.imthenico.annihilation.api.namespace.Namespace;

import java.util.Objects;

public class AuthorizationResult {

    private final boolean authorized;
    private final MessagePath reasonMessage;
    private final Namespace reason;

    public AuthorizationResult(
            boolean authorized,
            MessagePath reasonMessage,
            Namespace reason
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

    public Namespace getReason() {
        return reason;
    }

    public static AuthorizationResult of(boolean success, MessagePath errorMessage, Namespace reason) {
        return new AuthorizationResult(success, errorMessage, Objects.requireNonNull(reason));
    }

    public static AuthorizationResult of(boolean success, MessagePath errorMessage, String reason) {
        return of(success, errorMessage, Namespace.of(reason));
    }
}