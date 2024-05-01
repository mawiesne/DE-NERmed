package de.hhn.mi.dener.api;

import de.hhn.mi.commons.Pair;
import opennlp.tools.util.Span;

import java.util.List;

/**
 * Provides functionality to detect named entities.
 */
public interface NamedEntityRecognizer {

    /**
     * Checks for the named entities in a given {@code text}.
     *
     * @param text
     *          The text that will be used for detection - must not be {@code null}.
     *
     * @return The (medical) {@link Span named entities} for the {@code text}, or an empty list if none are found.
     *
     * @throws AssertionError Thrown if the given parameters were invalid.
     */
    List<Pair<String, Span>> detect(CharSequence text);

}
