package de.hhn.mi.dener.service;

import de.hhn.mi.dener.api.NamedEntityRecognizer;
import de.hhn.mi.dener.api.ModelSearchService;
import de.hhn.mi.dener.api.ModelService;
import de.hhn.mi.dener.api.ModelType;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class OpenNLPModelServiceImpl implements ModelService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OpenNLPModelServiceImpl.class);

    private final ModelSearchService nlpModelClassPathService;

    private final Map<ModelType, NamedEntityRecognizer> medEntityRecognizers = new HashMap<>();

    public OpenNLPModelServiceImpl(final ModelSearchService nlpModelClassPathService) {
        assert nlpModelClassPathService!=null;
        this.nlpModelClassPathService = nlpModelClassPathService;
    }

    @Override
    public NamedEntityRecognizer loadNamedEntityRecognizer(Locale locale) {
        return loadNamedEntityRecognizer(locale, ModelType.NER_MODEL_DEFAULT);
    }

    @Override
    public NamedEntityRecognizer loadNamedEntityRecognizer(Locale locale, ModelType type) {
        if (medEntityRecognizers.containsKey(type)) {
            return medEntityRecognizers.get(type);
        } else {
            boolean modelLoaded = false;
            try {
                for (Map.Entry<String, URL> entry : nlpModelClassPathService.findModels(type).entrySet()) {
                    String fileName = entry.getKey();
                    if (!fileName.toLowerCase(Locale.GERMAN).startsWith(locale.getLanguage())) {
                        continue;
                    }
                    try (InputStream wrappedStream = new BufferedInputStream(entry.getValue().openStream())) {
                        logger.info("Importing NLP model file '{}' ...", entry.getKey());
                        NamedEntityRecognizer namedEntityRecognizer =
                                new OpenNLPNamedEntityRecognizerImpl(new TokenNameFinderModel(wrappedStream), locale);
                        if (!medEntityRecognizers.containsKey(type)) {
                            medEntityRecognizers.put(type, namedEntityRecognizer);
                        }
                        logger.debug("Cached NamedEntityRecognizer instances = {}", medEntityRecognizers.size());
                        modelLoaded = true;
                        break;

                    } catch (InvalidFormatException e) {
                        logger.warn("Skipping {} because it does not meet the NLP format conventions!", fileName, e);
                    }
                }
            } catch(IOException e) {
                throw new RuntimeException("Could not detect NLP models due to I/O issues!" + e.getLocalizedMessage(), e);
            }
            if (modelLoaded) {
                return medEntityRecognizers.get(type);
            } else {
                throw new RuntimeException("No appropriate NamedEntityRecognizer models found in the classpath!");
            }
        }
    }
}
