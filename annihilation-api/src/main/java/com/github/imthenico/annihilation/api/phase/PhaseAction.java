package com.github.imthenico.annihilation.api.phase;

import com.github.imthenico.annihilation.api.game.Game;

import java.util.function.BiConsumer;

public interface PhaseAction extends BiConsumer<Phase, Game> {}