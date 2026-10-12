package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Locale;

public record DatabaseConfig(
    String host, int port, String databaseName, String username, String password, String sslMode) {
  private static final String URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?sslMode=%s&serverTimezone=UTC&allowPublicKeyRetrieval=true";

  public DatabaseConfig(
      String host, int port, String databaseName, String username, String password) {
    this(host, port, databaseName, username, password, "disable");
  }

  public String buildJdbcUrl() {
    return String.format(URL_TEMPLATE, host, port, databaseName, toMySqlSslMode(sslMode));
  }

  /** Acepta valores estilo MySQL (VERIFY_IDENTITY) o estilo PostgreSQL (disable, require...). */
  private static String toMySqlSslMode(final String value) {
    if (value == null || value.isBlank()) {
      return "DISABLED";
    }
    return switch (value.trim().toLowerCase(Locale.ROOT)) {
      case "disable", "disabled", "false" -> "DISABLED";
      case "prefer", "preferred" -> "PREFERRED";
      case "require", "required", "true" -> "REQUIRED";
      case "verify-ca", "verify_ca" -> "VERIFY_CA";
      case "verify-full", "verify_identity" -> "VERIFY_IDENTITY";
      default -> value.trim().toUpperCase(Locale.ROOT);
    };
  }
}
