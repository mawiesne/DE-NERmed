package de.hhn.mi.dener.api;

import java.util.Locale;

public interface ModelService {

    /**
     * @param locale Must not be {@code null}.
     * @return A valid {@link NamedEntityRecognizer} instance for {@link ModelType#NER_MODEL_DEFAULT default model}.
     *
     * @throws RuntimeException Thrown if runtime errors occurred upon load time.
     */
    NamedEntityRecognizer loadNamedEntityRecognizer(Locale locale);

    /**
     * @param locale Must not be {@code null}.
     * @return A valid {@link NamedEntityRecognizer} instance for the given {@link ModelType model type}.
     *
     * @throws RuntimeException Thrown if runtime errors occurred upon load time.
     */
    NamedEntityRecognizer loadNamedEntityRecognizer(Locale locale, ModelType type);

}
