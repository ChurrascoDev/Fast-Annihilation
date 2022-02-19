package com.github.imthenico.annihilation.api.provider;

import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;

import java.util.Optional;
import java.util.Set;

public interface WorldTemplateLoader {

    Optional<LoadedWorldTemplate> newTemplate(String name);

    Set<LoadedWorldTemplate> getAllTemplates();

}