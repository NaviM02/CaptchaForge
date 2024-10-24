package com.navi.captchaapi.data;

import com.navi.captchaapi.model.Captcha;
import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.db.*;

import java.io.*;
import java.nio.file.Files;

public class Connection {
    private static boolean create = false;
    private static Reader reader;
    private static DBLexer lexer;
    private static DBParser parser;
    public static String text;

    public static String readFileAsString(File file) {
        if (file.exists()) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n"); // Agregar línea al StringBuilder
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
                return null;
            }
            return content.toString();
        } else {
            System.out.println("El archivo no existe: " + file.getAbsolutePath());
            return "";
        }
    }
    public static void createDB(){
        if(create) return;
        File userHome = new File(System.getProperty("user.home"));
        String appFolderName = "Captchas";
        File appFolder = new File(userHome, appFolderName);

        if(!appFolder.exists()) {
            appFolder.mkdirs();
        }

        String userText = "db.captchas(\n)";
        File file = new File(appFolder, "captchas.db");
        if(!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(userText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        create = true;
    }

    public static DBParser connectDB() {
        File userHome = new File(System.getProperty("user.home"));
        String appFolderName = "Captchas/captchas.db";
        File appFolder = new File(userHome, appFolderName);

        text = readFileAsString(appFolder);
        if(text == null) text = "";

        reader = new StringReader(text);
        lexer = new DBLexer(reader);
        parser = new DBParser(lexer);
        try{
            ErrorsLP.clearErrors();
            parser.parse();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return parser;
    }

    public static Captcha parse(String text) {
        reader = new StringReader(text);
        lexer = new DBLexer(reader);
        parser = new DBParser(lexer);
        try{
            ErrorsLP.clearErrors();
            parser.parse();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        if(parser.captchas.isEmpty()) return null;
        return parser.captchas.get(0);
    }

    public static int calculatePosition(String text, int line, int column) {
        int position = 0;
        for (int i = 1; i < line; i++) {
            position = text.indexOf('\n', position) + 1; // Avanzar hasta el inicio de la siguiente línea
        }
        return position + (column - 1);
    }

    public static void insertTextUser(int position, String textToInsert){
        File userHome = new File(System.getProperty("user.home"));
        String appFolderName = "Captchas/captchas.db";
        File appFolder = new File(userHome, appFolderName);
        try {
            insertTextAtPosition(appFolder, position, textToInsert);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateTextUser(int startPosition, int endPosition, String textToInsert){
        File userHome = new File(System.getProperty("user.home"));
        String appFolderName = "Captchas/captchas.db";
        File appFolder = new File(userHome, appFolderName);
        try {
            replaceTextBetweenPositions(appFolder, startPosition, endPosition, textToInsert);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteTextUser(int startPosition, int endPosition){
        File userHome = new File(System.getProperty("user.home"));
        String appFolderName = "Captchas/users.db";
        File appFolder = new File(userHome, appFolderName);
        try {
            removeTextBetweenPositions(appFolder, startPosition, endPosition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void insertTextAtPosition(File file, int position, String textToInsert) throws IOException {
        // Leer el contenido del archivo
        String content = new String(Files.readAllBytes(file.toPath()));

        // Validar la posición
        if (position < 0 || position > content.length()) {
            throw new IllegalArgumentException("Position out of bounds");
        }

        // Insertar el nuevo texto en la posición deseada
        String newContent = content.substring(0, position) + textToInsert + content.substring(position);

        // Escribir el contenido modificado de nuevo en el archivo
        Files.write(file.toPath(), newContent.getBytes());
    }
    private static void replaceTextBetweenPositions(File file, int startPosition, int endPosition, String newText) throws IOException {
        // Leer el contenido del archivo
        String content = new String(Files.readAllBytes(file.toPath()));

        // Validar las posiciones
        if (startPosition < 0 || endPosition < startPosition || endPosition > content.length()) {
            throw new IllegalArgumentException("Invalid positions");
        }

        String newContent = content.substring(0, startPosition) + newText + content.substring(endPosition);

        // Escribir el contenido modificado de nuevo en el archivo
        Files.write(file.toPath(), newContent.getBytes());
    }
    private static void removeTextBetweenPositions(File file, int startPosition, int endPosition) throws IOException {
        // Leer el contenido del archivo
        String content = new String(Files.readAllBytes(file.toPath()));

        // Validar las posiciones
        if (startPosition < 0 || endPosition < startPosition || endPosition > content.length()) {
            throw new IllegalArgumentException("Invalid positions");
        }

        // Crear el nuevo contenido eliminando el texto entre las posiciones
        String newContent = content.substring(0, startPosition) + content.substring(endPosition);

        // Escribir el contenido modificado de nuevo en el archivo
        Files.write(file.toPath(), newContent.getBytes());
    }
}
