package de.hhn.mi.dener.service;

import de.hhn.mi.commons.Pair;
import de.hhn.mi.dener.api.NamedEntityRecognizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * A NamedEntityRecognizer implementation using OpenNLP library via {@link NameFinderME}.
 */
public class OpenNLPNamedEntityRecognizerImpl implements NamedEntityRecognizer {

  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(OpenNLPNamedEntityRecognizerImpl.class);

  private static final Pattern WORD_PATTERN = Pattern.compile("(?!\\s)[^A-Za-z0-9äöüßÄÖÜ_]");

  private final NameFinderME entityFinder;

  OpenNLPNamedEntityRecognizerImpl(final TokenNameFinderModel nerModel, final Locale modelLocale) {
    assert nerModel != null;
    assert modelLocale != null;
    this.entityFinder = new NameFinderME(nerModel);
    if (logger.isDebugEnabled()) {
      logger.debug("{} NamedEntityRecognizer initialization... [OK]", modelLocale.getDisplayCountry());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Pair<String, Span>> detect(CharSequence text) {
    // Note: A simple \\W in the regex is not valid for the German language as this is rather umlaut-driven.
    String prepared = WORD_PATTERN.matcher(text).replaceAll(" $0");
    String[] words = prepared.split("\\s+");
    Span[] spans = entityFinder.find(words);

    List<Pair<String, Span>> result = new ArrayList<>();
    for (Span s : spans) {
      String ne = words[s.getStart()];
      result.add(Pair.of(ne, s));
    }
    return result;
  }

}
