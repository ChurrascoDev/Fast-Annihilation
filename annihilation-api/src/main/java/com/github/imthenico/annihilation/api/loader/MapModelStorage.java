package com.github.imthenico.annihilation.api.loader;

import com.github.imthenico.annihilation.api.exception.NoPropertiesFoundException;
import com.github.imthenico.annihilation.api.exception.UnknownWorldException;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.repository.service.DeletionService;
import com.github.imthenico.simplecommons.data.repository.service.FindService;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;

public interface MapModelStorage extends FindService<ConfigurableModel>, DeletionService, SavingService<ConfigurableModel> {

    /**
     * Find a map model using its name
     * @param sourceKey - The key containing the map name
     * @return The model found
     * @throws UnsupportedOperationException when this method is called
     * from another thread.
     */
    @Override
    ConfigurableModel usingId(SourceKey sourceKey)
            throws UnsupportedOperationException, NoPropertiesFoundException, UnknownWorldException;
}