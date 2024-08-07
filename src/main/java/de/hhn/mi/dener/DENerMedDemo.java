package de.hhn.mi.dener;

import de.hhn.mi.commons.Pair;
import de.hhn.mi.dener.api.NamedEntityRecognizer;
import de.hhn.mi.dener.api.ModelSearchService;
import de.hhn.mi.dener.api.ModelService;
import de.hhn.mi.dener.api.ModelType;
import de.hhn.mi.dener.service.ModelSearchServiceImpl;
import de.hhn.mi.dener.service.OpenNLPModelServiceImpl;
import opennlp.tools.util.Span;
import org.slf4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class DENerMedDemo {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(DENerMedDemo.class);

  private static NamedEntityRecognizer NER_SERVICE;

  private static final List<String> DEMO_TEXTS =
          List.of(// Example 1
                  "Der Urin des Patienten ist rot verfärbt.",
                  // Example 2
                  "Die Patientin verneinte ebenfalls Husten mit Auswurf oder Brennen während Wasserlassens. " +
                          "Sie hatte jedoch deutlich erhöhte Temperatur und Schüttelfrost in den letzten Tagen. ",
                  // Example 3
                  "Nach Aufnahme fetter Nahrung gibt sie Bauchschmerzen an. " +
                          "Gestern Abend Beginn mit Husten und Übelkeit, dabei sporadisch Druckgefühl. " +
                          "Um 9 Uhr Schwindel und eingeschränkte Ansprechbarkeit. " +
                          "Thorakale Schmerzen mit Ausstrahlung in den Rücken, die nach Nitrogabe besser wurden. " +
                          "Intermittierend im Rahmen des Herzrasens kam es zu einem leichten retrosternalen Brennen. " +
                          "Übelkeit oder Brechreiz werden ebenso verneint wie Synkopen oder Dyspnoe. ",
                  // Example 4
                  "Die Patientin stellte sich gestern bereits wegen starker Schmerzen am ganzen Körper vor. " +
                          "Am Morgen nach dem Aufstehen deutlich erhöhter Puls, gegen Nachmittag wohl thorakaler Druck, " +
                          "auch im linken Arm, Unwohlsein und tachykarder Herzschlag.");

  public static void main(String[] args) {
    ModelSearchService searchService = new ModelSearchServiceImpl();
    ModelService modelService = new OpenNLPModelServiceImpl(searchService);
    NER_SERVICE = modelService.loadNamedEntityRecognizer(Locale.GERMAN, ModelType.NER_MODEL_DEFAULT);

    for (String s : DEMO_TEXTS) {
      LOG.info("Detecting NEs for: '{}'", s);
      processInput(s);
      LOG.info("");
    }
    LOG.info("");
    LOG.info("Entering interactive mode, press 'q' to quit.");
    Scanner scanner = new Scanner(System.in);
    while (true){
      String s = scanner.nextLine();
      if(s.equals("q")) {
        break;
      } else {
        try {
          processInput(s);
        } catch (RuntimeException e) {
          LOG.warn(e.getLocalizedMessage());
        }
      }
      LOG.info("");
    }
    LOG.info("Shutting down...");
    scanner.close();
  }

  private static void processInput(String text) {
    List<Pair<String, Span>> entities =  NER_SERVICE.detect(text);
    for (Pair<String, Span> e : entities) {
      String word = e.getFirst();
      Span span = e.getSecond();
      LOG.info("Found NE '{}' - [pos: {}, prob: {}]", word, span.getEnd(), String.format("%.2f", span.getProb()));
    }
  }
}
