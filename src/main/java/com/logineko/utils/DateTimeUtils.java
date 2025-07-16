package com.logineko.utils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateTimeUtils {
  public static final DateTimeFormatter EXTERNAL_SOURCE_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);

  private DateTimeUtils() {}
}
