package de.hhn.mi.dener.api;

import java.util.stream.Stream;

public enum ModelType {

  NER_MODEL_DEFAULT("DE-NERmed-Wiki_2023-maxent"),
  NER_MODEL_WIKI("DE-NERmed-Wiki_2023-maxent"),
  NER_MODEL_WIKI_MEDIUM("DE-NERmed-Wiki_2023_medium-maxent"),
  NER_MODEL_WIKI_SMALL("DE-NERmed-Wiki_2023_small-maxent");

  private final String textValue;

  ModelType(String textValue) {
    this.textValue = textValue;
  }

  @Override
  public String toString() {
    return textValue;
  }

  public static ModelType fromString(String textValue) {
    return Stream.of(ModelType.values())
            .filter(e -> e.textValue.equalsIgnoreCase(textValue))
            .findFirst()
            .orElse(NER_MODEL_DEFAULT);
  }
}
