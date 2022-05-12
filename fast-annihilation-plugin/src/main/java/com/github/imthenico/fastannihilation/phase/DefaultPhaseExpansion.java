package com.github.imthenico.fastannihilation.phase;

import java.util.Objects;

import com.github.imthenico.annihilation.api.phase.PhaseActionFactory;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import me.yushust.message.MessageHandler;

public class DefaultPhaseExpansion implements PhaseExpansion {

    private final MessageHandler messageHandler;

    public DefaultPhaseExpansion(MessageHandler messageHandler) {
        this.messageHandler = Objects.requireNonNull(messageHandler);
    }

    @Override
    public int[] supportedPhases() {
        return new int[] {1, 2, 3, 4, 5};
    }

    @Override
    public PhaseActionFactory getPhaseActionFactory() {
        return new DefaultPhaseFactory(messageHandler);
    }
}