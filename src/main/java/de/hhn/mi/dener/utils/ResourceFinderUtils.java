package de.hhn.mi.dener.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides helper methods that enable the detection of model resources.
 */
public final class ResourceFinderUtils {

  /**
   * Scans the classpath for files that match the given {@code fileNameSuffix}.
   *
   * @param fileNameSuffix A file name suffix that must not start with a {@code DOT} character!
   *                       Must not be {@code null}.
   * @return A Map of resources that match the given {@code fileNameSuffix} which can be referenced via a {@link URL}.
   */
  public static Map<String, URL> findModelResources(String fileNameSuffix) throws IOException {
    return findResourcesInDirectory("models", fileNameSuffix);
  }

  /**
   * Scans a directory for files that match the given {@code fileNameSuffix}.
   *
   * @param directory  The name of the directory to scan. Note: Scanning relative of executables.
   * @param fileNameSuffix A file name suffix to filter resources with. Must not be {@code null}.
   * @return A Map of resources that match the given {@code fileNameSuffix} which can be referenced via a {@link URL}.
   */
  public static Map<String, URL> findResourcesInDirectory(String directory, String fileNameSuffix) throws IOException {
    final Map<String, URL> resources = new TreeMap<>();
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(Path.of(directory))) {
      for (Path child : ds) {
        String resource = child.getFileName().toString();
        if (resource.endsWith(fileNameSuffix)) {
          resources.put(resource, child.toUri().toURL());
        }
      }
    }
    return resources;
  }
}