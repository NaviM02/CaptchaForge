package com.navi.captchaapi.parser_lexer.cc.obj;
import lombok.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter @Setter @ToString
public class Label {
    public static final int HTML = 1;
    public static final int HEAD = 2;
    public static final int TITLE = 3;
    public static final int LINK = 4;
    public static final int BODY = 5;
    public static final int SPAN = 6;
    public static final int INPUT = 7;
    public static final int TEXTAREA = 8;
    public static final int SELECT = 9;
    public static final int OPTION = 10;
    public static final int DIV = 11;
    public static final int IMG = 12;
    public static final int BR = 13;
    public static final int BUTTON = 14;
    public static final int H1 = 15;
    public static final int P = 16;
    public static final int SCRIPT = 17;

    public static final String[] LABELS = {"HTML", "HEAD", "TITLE", "LINK", "BODY", "SPAN", "INPUT",
        "TEXTAREA", "SELECT", "OPTION", "DIV", "IMG", "BR", "BUTTON", "H1", "P", "SCRIPT"};

    private Location loc;
    private int type;
    private ArrayList<Parameter> parameters;
    private ArrayList<Object> content;

    public Label(){

    }
    public Label(int line, int col, int type){
        this.loc = new Location(line, col);
        this.type = type;
        this.parameters = new ArrayList<>();
        this.content = new ArrayList<>();
    }
    public Label(int line, int col, int type, ArrayList<Object> content){
        this.loc = new Location(line, col);
        this.type = type;
        this.parameters = new ArrayList<>();
        this.content = content;
    }
    public Label(int line, int col, int type, ArrayList<Parameter> parameters, ArrayList<Object> content){
        this.loc = new Location(line, col);
        this.type = type;
        this.parameters = parameters;
        this.content = content;
    }

    public String toHtml(StringBuilder script){
        boolean isBody = false;
        parameters.forEach(Parameter::getParam);
        StringBuilder html = new StringBuilder();
        if(this.type != INPUT && this.type != BR){
            html.append("<").append(LABELS[type-1].toLowerCase()).append(" ").append(paramsText()).append(">\n");
            for(Object body: content){
                if(body instanceof String str){
                    if(this.type == TITLE){
                        insertTitleFunction(script, str);
                    }
                    html.append(str).append(" ");
                }
                else if(body instanceof Label label){
                    if(label.getType() == Label.BODY){
                        isBody = true;
                    }
                    html.append((label).toHtml(script));
                }
            }
            if(isBody) html.append(script).append("\n");
            if(this.type == Label.HEAD) html.append(generalStyle());
            html.append("</").append(LABELS[type-1].toLowerCase()).append(">\n");
        }
        else{
            html.append("<").append(LABELS[type-1].toLowerCase()).append(" ").append(paramsText()).append(">\n");
        }
        return html.toString();
    }
    public String functions(){
        String insert = """
                function INSERT(text) {
                    const container = document.getElementById('captcha-container');
                    if (container) {
                        container.insertAdjacentHTML('beforeend', text);
                    } else {
                        document.body.insertAdjacentHTML('beforeend', text);
                    }
                }
                """;
        String asc = """
                function ASC(text){
                    return text.split('').sort().join('');
                }
                """;
        String desc = """
                function DESC(text){
                    return text.split('').sort().reverse().join('');
                }
                """;
        String letPar = """
                function LETPAR_NUM(text){
                    return text.split('').map((letra, index) => {
                        if (index % 2 === 0) {
                          return letra.charCodeAt(0);
                        }
                        return letra;
                    }).join('');
                }
                """;
        String letNotPar = """
                function LETIMPAR_NUM(text){
                    return text.split('').map((letra, index) => {
                        if (index % 2 !== 0) {
                          return letra.charCodeAt(0);
                        }
                        return letra;
                    }).join('');
                }
                """;
        String reverse = """
                function REVERSE(text) {
                  return text.split('').reverse().join('');
                }
                """;
        String randomChar = """
                function CARACTER_ALEATORIO() {
                  const codigoAleatorio = Math.random() < 0.5 ?
                    Math.floor(Math.random() * 26) + 65 :
                    Math.floor(Math.random() * 26) + 97;
                  return String.fromCharCode(codigoAleatorio);
                }
                """;
        String randomNum = """
                function NUM_ALEATORIO() {
                  return Math.floor(Math.random() * 10);
                }
                """;
        String alertInfo = """
                function ALERT_INFO(msj) {
                  alert(msj);
                }
                """;
        String exit = """
                function EXIT() {
                  return;
                }
                """;
        String redirect = """
                function REDIRECT() {
                  window.location.href = url;
                }
                """;
        return insert + asc + desc + letPar + letNotPar + reverse + randomChar + randomNum + alertInfo + exit + redirect;
    }
    public String paramsText(){
        StringBuilder params = new StringBuilder();
        String initStyle = "";
        String endStyle = "\"";
        StringBuilder style = new StringBuilder();
        switch (this.type){
            case HEAD, BR, OPTION, TITLE, LINK -> {
                initStyle = "";
                endStyle = "";
            }
            case BODY, DIV -> style = new StringBuilder("style = \"background: #F5F5DC; box-sizing: border-box; display: flex; flex-direction: column; align-items: center; color: #333;");
            case BUTTON -> initStyle = "style = \"";
            default -> initStyle = "style = \"padding: 3px; margin 10px; ";
        }
        for (Parameter parameter : parameters){
            if(parameter.getType() == Parameter.ID || parameter.getType() == Parameter.HREF || parameter.getType() == Parameter.NAME || parameter.getType() == Parameter.ONCLICK){
                params.append(parameter.getId()).append(" ");
            }
            else{
                if(parameter.getType() == Parameter.CLASS){
                    String val = parameter.getValue();
                    if(val.equals("row")) params.append("flex-direction: row");
                    else params.append("flex-direction: column");
                }
                else style.append(parameter.getParam()).append(" ");
            }
        }
        return params + initStyle + style + endStyle;
    }

    public String getName(){
        for(Parameter parameter: parameters){
            if(parameter.getType() == Parameter.NAME) return parameter.getValue();
        }
        return "";
    }
    public String getId(){
        for(Parameter parameter: parameters){
            if(parameter.getType() == Parameter.ID) return parameter.getValue();
        }
        return "";
    }

    public String generalStyle(){
        return """
                <style>
                    button {
                        margin: 10px;
                        padding: 10px 20px;
                        border-radius: 15px;
                        border: none;
                        background-color: #39C5BB;
                        color: white;
                        font-weight: bold;
                        cursor: pointer;
                        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
                        transition: background-color 0.3s ease, box-shadow 0.3s ease;
                    }
            
                    button:hover {
                        background-color: #5ee5d5;
                        box-shadow: 0px 6px 8px rgba(0, 0, 0, 0.2);
                    }
                </style>
                """;
    }

    public void insertTitleFunction(StringBuilder script, String title) {
        String titleFunction = "\nfunction changeTitle(newTitle) {\n" +
                "    document.title = newTitle;\n" +
                "}\n" +
                "changeTitle('" + title + "');\n";

        int scriptClosePos = script.lastIndexOf("</script>");

        if (scriptClosePos != -1) {
            script.insert(scriptClosePos, titleFunction);
        }
        else {
            script.append(titleFunction);

        }
    }
}