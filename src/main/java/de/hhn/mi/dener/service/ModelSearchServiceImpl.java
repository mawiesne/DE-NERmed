package de.hhn.mi.dener.service;

import de.hhn.mi.dener.api.ModelSearchService;
import de.hhn.mi.dener.api.ModelType;
import de.hhn.mi.dener.utils.ResourceFinderUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link ModelSearchService} implementation that detects NER model files.
 */
public final class ModelSearchServiceImpl implements ModelSearchService {

  private static final String FILE_NAME_SUFFIX = "bin";

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, URL> findModels(ModelType type) {
    return detectModels(type);
  }

  private Map<String, URL> detectModels(ModelType type) {
    Map<String, URL> found = new HashMap<>();
    try {
      for(Map.Entry<String, URL> e : ResourceFinderUtils.findModelResources(FILE_NAME_SUFFIX).entrySet()) {
        if(e.getKey().contains(type.toString())) {
          found.put(e.getKey(), e.getValue());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return found;
  }

}
