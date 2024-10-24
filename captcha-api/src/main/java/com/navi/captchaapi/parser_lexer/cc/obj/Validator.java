package com.navi.captchaapi.parser_lexer.cc.obj;

import com.navi.captchaapi.parser_lexer.ErrorsLP;

import java.util.*;
import java.util.regex.Pattern;

public class Validator {
    private static final HashMap<String, String> colorMap = new HashMap<>();
    static {
        colorMap.put("black", "#000000");
        colorMap.put("olive", "#808000");
        colorMap.put("teal", "#008080");
        colorMap.put("red", "#FF0000");
        colorMap.put("blue", "#0000FF");
        colorMap.put("maroon", "#800000");
        colorMap.put("navy", "#000080");
        colorMap.put("gray", "#808080");
        colorMap.put("lime", "#00FF00");
        colorMap.put("fuchsia", "#FF00FF");
        colorMap.put("green", "#008000");
        colorMap.put("white", "#FFFFFF");
        colorMap.put("purple", "#800080");
        colorMap.put("silver", "#C0C0C0");
        colorMap.put("yellow", "#FFFF00");
        colorMap.put("aqua", "#00FFFF");
    }
    private static final Pattern hexPattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");
    private static final Pattern pixelPattern = Pattern.compile("^\\d+px$");
    private static final HashSet<String> validFonts = new HashSet<>();
    static {
        validFonts.add("Courier");
        validFonts.add("Verdana");
        validFonts.add("Arial");
        validFonts.add("Geneva");
        validFonts.add("sans-serif");
    }
    private static final HashSet<String> validAlignments = new HashSet<>();
    static {
        validAlignments.add("left");
        validAlignments.add("right");
        validAlignments.add("center");
        validAlignments.add("justify");
    }
    private static final HashSet<String> validInputTypes = new HashSet<>();
    static {
        validInputTypes.add("text");
        validInputTypes.add("number");
        validInputTypes.add("radio");
        validInputTypes.add("checkbox");
    }
    private static final HashSet<String> classes = new HashSet<>();
    static {
        classes.add("row");
        classes.add("column");
    }
    private static final Pattern dimensionPattern = Pattern.compile("^(\\d+px|\\d+%)$");

    public static String validateAndTransform(String input, int line, int col) {
        if (colorMap.containsKey(input.toLowerCase())) {
            return colorMap.get(input.toLowerCase());
        }

        if (hexPattern.matcher(input).matches()) {
            return input;
        }

        ErrorsLP.addError(input, line, col, "Semántico", "Color inválido(Se esperaba: black, olive, teal, red, blue, maroon, navy, gray, lime, fuchsia, green, white, green, purple, silver, yellow o aqua)");
        return "#808080";
    }

    public static boolean validatePixels(String input, int line, int col) {
        if(pixelPattern.matcher(input).matches()) return true;

        ErrorsLP.addError(input, line, col, "Semántico", "Pixeles inválidos (Se esperaba: numero px)");
        return false;
    }
    public static boolean isValidFont(String input, int line, int col) {
        if(validFonts.contains(input)) return true;

        ErrorsLP.addError(input, line, col, "Semántico", "Fuente inválida (Se esperaba: Courier,Verdana ,Arial, Geneva o sans-serif");
        return false;
    }

    public static boolean isValidAlignment(String input, int line, int col) {
        if(validAlignments.contains(input)) return true;

        ErrorsLP.addError(input, line, col, "Semántico", "Alineación inválida (Se esperaba: left, right, center o justify");
        return false;
    }

    public static boolean isValidInputType(String input, int line, int col) {
        if(validInputTypes.contains(input)) return true;

        ErrorsLP.addError(input, line, col, "Semántico", "Tipo de input inválido (Se esperaba: text, number, radio o checkbox)");
        return false;
    }
    public static boolean isValidInteger(String input, int line, int col) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            ErrorsLP.addError(input, line, col, "Semántico", "Numero inválido");
            return false;
        }
    }
    public static boolean isValidClass(String input, int line, int col) {
        if(classes.contains(input)) return true;

        ErrorsLP.addError(input, line, col, "Semántico", "Clase inválida (Se esperaba: row o column)");
        return false;
    }

    public static boolean isValidDimension(String input, int line, int col) {
        if(dimensionPattern.matcher(input).matches()) return true;

        ErrorsLP.addError(input, line, col, "Semántico", "Dimensión inválida");
        return false;
    }
}
