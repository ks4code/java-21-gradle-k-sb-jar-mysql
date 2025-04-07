package com.sahajanand.java_21_gradle_k_sb_jar_mysql.utils;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

@Component
public class Utils {

  private static final boolean isDisableSystemOutPrintMethod = false;
  private static final String ANSI_WHITE_TEXT = "\u001B[37m";
  private static final String ANSI_RESET = "\u001B[0m";
  //  private static final String ANSI_BRIGHT_GREEN_TEXT = "\u001B[92m";
  private static final String ANSI_BRIGHT_CRAYON_TEXT = "\u001B[96m";
  private static final String ANSI_BLACK_TEXT_YELLOW_BACKGROUND = "\u001B[30;43m";
  private static final String ANSI_ITALIC_START = "\033[3m";
  private static final String ANSI_ITALIC_END = "\033[0m";

  private static final PrintStream originalOut = System.out;
  //  private static final PrintStream originalErr = System.err;

  /*// Encapsulated Null Output Stream
  private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
    @Override
    public void write(int b) {
      // Do nothing (suppress output)
    }
  };*/

  private static Environment environment;

  public Utils(Environment environment) {
    Utils.environment = environment;
  }

  public static String getDateTimeLocalCurrent(String format) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.now().format(dateTimeFormatter);
  }

  public static String getCurrentFileName() {
    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(frame -> {
      return frame.getFileName();
    }).orElse(""));
    //    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(StackWalker.StackFrame::getFileName).orElse(""));
  }

  public static String getCurrentClassName() {
    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(frame -> {
      return frame.getClassName();
    }).orElse(""));
    //    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(StackWalker.StackFrame::getClassName).orElse(""));
  }

  public static String getCurrentMethodName() {
    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(frame -> {
      return frame.getMethodName();
    }).orElse(""));

    //    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(StackWalker.StackFrame::getMethodName).orElse(""));
  }

  public static Integer getCurrentLineNumber() {
    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(frame -> {
      return frame.getLineNumber();
    }).orElse(null));
    //    return StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().map(StackWalker.StackFrame::getLineNumber).orElse(""));
  }

  public static StackWalker.StackFrame getCurrentStackFrame() {
    return getCurrentStackFrame(0);
  }

  public static StackWalker.StackFrame getCurrentStackFrame(long skipFrame) {
    return StackWalker.getInstance().walk(frames -> frames.skip(skipFrame).findFirst().orElse(null));
  }

  private static String objectFormat(Object obj) {
    if (obj == null) {
      return "null";
    }
    else if (obj instanceof String) {
      return "\"" + obj + "\"";
    }
    else if (obj instanceof int[]) {
      return Arrays.toString((int[]) obj);
    }
    else if (obj instanceof double[]) {
      return Arrays.toString((double[]) obj);
    }
    else if (obj instanceof float[]) {
      return Arrays.toString((float[]) obj);
    }
    else if (obj instanceof long[]) {
      return Arrays.toString((long[]) obj);
    }
    else if (obj instanceof short[]) {
      return Arrays.toString((short[]) obj);
    }
    else if (obj instanceof byte[]) {
      return Arrays.toString((byte[]) obj);
    }
    else if (obj instanceof char[]) {
      return Arrays.toString((char[]) obj);
    }
    else if (obj instanceof boolean[]) {
      return Arrays.toString((boolean[]) obj);
    }
    else if (obj instanceof Object[]) {
      // Recursively format object arrays
      return Arrays.deepToString(Arrays.stream((Object[]) obj).map(Utils::objectFormat).toArray());
    }
    else if (obj instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) obj;
      StringBuilder sb = new StringBuilder("{");
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        sb.append(objectFormat(entry.getKey())).append(": ").append(objectFormat(entry.getValue())).append(", ");
      }
      if (!map.isEmpty())
        sb.setLength(sb.length() - 2); // Remove last comma
      sb.append("}");
      return sb.toString();
    }
    else if (obj instanceof Collection) {
      // Recursively format collections (List, Set, etc.)
      Collection<?> collection = (Collection<?>) obj;
      return "[" + collection.stream().map(Utils::objectFormat).toList() + "]";
    }
    else if (obj.getClass() == Object.class) {
      // Explicitly empty object
      return "{}";
    }
    return obj.toString(); // Default case for non-collections
  }

  /* // Not showing proper location
  public static void systemOutPrint(String format) {
//    String datePrefix = getDateTimeLocalCurrent("hh:mm:ss.SSSSSSSSS a") + " => ";
//    System.out.println("\n" + Utils.getCurrentStackFrame(2));
//
//    String datePrefixColored = ANSI_WHITE_TEXT + datePrefix + ANSI_RESET;
//    String formatColored = ANSI_BLACK_TEXT_YELLOW_BACKGROUND + " " + format + " " + ANSI_RESET;
//
//    System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " ~~ \n");

    systemOutPrint(format, (Object[]) null);
  }*/

  @SafeVarargs
  public static <T> void systemOutPrint(String format, T... args) {
    if (isDisableSystemOutPrintMethod) {
      return;
    }

    String datePrefix = getDateTimeLocalCurrent("hh:mm:ss.SSSSSSSSS a") + " => ";
    System.out.println("\n" + Utils.getCurrentStackFrame(2));

    //    format = format.trim();
    int lastSpaceIndex = format.lastIndexOf(' ');

    //    String format1 = format.substring(0, format.length() - 6).trim();
    //    String format2 = format.substring(format.length() - 6);
    String format1 = format.substring(0, lastSpaceIndex);
    String format2 = format.substring(lastSpaceIndex + 1);

    String datePrefixColored = ANSI_WHITE_TEXT + datePrefix + ANSI_RESET;
    String formatColored = ANSI_BLACK_TEXT_YELLOW_BACKGROUND + " " + format1 + " " + ANSI_RESET;

    if (args == null) {
      System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " %s \n", format2);
      return;
    }

    if (args.length == 1) {
      //      System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " %s %s\n", format2, ANSI_BRIGHT_GREEN_TEXT + objectFormat(args[0]) + ANSI_RESET);
      //      System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " %s %s\n", format2, ANSI_BRIGHT_CRAYON_TEXT + objectFormat(args[0]) + ANSI_RESET);
      System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " %s " + ANSI_ITALIC_START + "%s" + ANSI_ITALIC_END + "\n", format2, ANSI_BRIGHT_CRAYON_TEXT + objectFormat(args[0]) + ANSI_RESET);
    }
    else {
      //      System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " %s %s\n", format2, ANSI_BRIGHT_GREEN_TEXT + Arrays.deepToString(Arrays.stream(args).map(Utils::objectFormat).toArray()) + ANSI_RESET);
      System.out.printf(Locale.getDefault(), datePrefixColored + formatColored + " %s " + ANSI_ITALIC_START + "%s" + ANSI_ITALIC_END + "\n", format2, ANSI_BRIGHT_CRAYON_TEXT + Arrays.deepToString(Arrays.stream(args).map(Utils::objectFormat).toArray()) + ANSI_RESET);
    }
  }

  //  public static void disableAllSystemOutSystemErr(boolean isDisable) {
  public static void disableAllSystemOut(boolean isDisable) {
    if (isDisable) {
      //      systemOutPrint("Disabled all System.out System.err");
      systemOutPrint("Disabled all System.out");

      //      System.setOut(new PrintStream(NULL_OUTPUT_STREAM));
      //      System.setErr(new PrintStream(NULL_OUTPUT_STREAM));

      System.setOut(new PrintStream(OutputStream.nullOutputStream()));
      //      System.setErr(new PrintStream(OutputStream.nullOutputStream()));
    }
    else {
      System.setOut(originalOut);
      //      System.setErr(originalErr);
    }
  }

  public static void consoleClear() {
    //Not Work
    //    System.out.print("\033[H\033[2J");
    //    System.out.flush();

  }

  public static String[] getActiveProfiles() {
    String[] activeProfiles = environment.getActiveProfiles();

    if (activeProfiles.length > 0) {
      System.out.println("activeProfiles ***** " + Arrays.toString(activeProfiles));
      return activeProfiles;
    }
    else {
      System.out.println("No active profile set, Please set profile in src/main/resources/application.properties");
      return new String[0];
    }
  }

  public static boolean isDev() {
    return environment.acceptsProfiles(Profiles.of("dev"));
  }

  public static boolean isProd() {
    return environment.acceptsProfiles(Profiles.of("prod"));
  }
}
