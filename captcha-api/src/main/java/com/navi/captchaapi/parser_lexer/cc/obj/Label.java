package com.navi.captchaapi.parser_lexer.cc.obj;
import lombok.*;

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
        parameters.forEach(Parameter::getParamStyle);
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
        String setVariable = """
                function setVariable(id, line, col, type, value, mode, procedure, execution) {
                    const existingVariable = globalVariables.find(variable => variable.id === id && variable.procedure === procedure);
                    if (existingVariable) {
                    existingVariable.execution += 1;
                    } else {
                    globalVariables.push({ id, line, col, type, value, mode, procedure, execution });
                    }
                }
                """;
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
        String redirect = """
                function REDIRECT() {
                  if (typeof window.beforeRedirect === 'function') {
                    window.beforeRedirect(url);
                  }
                }
                """;
        String show = """
                function show() {
                    const table = document.getElementById("variables-table").getElementsByTagName('tbody')[0];
                    table.innerHTML = "";
            
                    globalVariables.forEach((variable, index) => {
                        const row = table.insertRow();
            
                        row.insertCell().textContent = index + 1;
                        row.insertCell().textContent = variable.id;
                        row.insertCell().textContent = variable.line;
                        row.insertCell().textContent = variable.col;
                        row.insertCell().textContent = variable.type;
                        row.insertCell().textContent = variable.value?.v !== undefined ? variable.value.v : variable.value;
                        row.insertCell().textContent = variable.mode;
                        row.insertCell().textContent = variable.procedure.startsWith("funcion ")
                        ? variable.procedure.slice(8)
                        : variable.procedure;
                        row.insertCell().textContent = variable.execution;
                    });
                    console.log(globalVariables);
                }
                """;
        return setVariable + insert + asc + desc + letPar + letNotPar + reverse + randomChar + randomNum + alertInfo + redirect + show;
    }
    public String paramsText()  {
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
            if(parameter.getType() == Parameter.ID || parameter.getType() == Parameter.HREF ||
                    parameter.getType() == Parameter.NAME || parameter.getType() == Parameter.ONCLICK ||
                    parameter.getType() == Parameter.ALT || parameter.getType() == Parameter.SRC){
                params.append(parameter.getParamLabel()).append(" ");
            }
            else{
                if(parameter.getType() == Parameter.CLASS){
                    String val = parameter.getValue();
                    if(val.equals("row")) params.append("flex-direction: row");
                    else params.append("flex-direction: column");
                }
                else style.append(parameter.getParamStyle()).append(" ");
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
                    textarea {
                        width: 100%;
                        padding: 10px;
                        font-size: 16px;
                        font-family: Arial, sans-serif;
                        color: #333;
                        border: 1px solid #ccc;
                        border-radius: 4px;
                        resize: vertical; /* Permite cambiar solo el alto */
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                
                    textarea:focus {
                        outline: none;
                        border-color: #1abc9c; /* Color verde Miku */
                        box-shadow: 0 0 5px rgba(26, 188, 156, 0.5);
                    }
                    select {
                        width: 100%;
                        padding: 10px;
                        font-size: 16px;
                        font-family: Arial, sans-serif;
                        color: #333;
                        border: 1px solid #ccc;
                        border-radius: 4px;
                        background-color: #fff;
                        appearance: none; 
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                
                    select:focus {
                        outline: none;
                        border-color: #1abc9c;
                        box-shadow: 0 0 5px rgba(26, 188, 156, 0.5);
                    }
                
                    select option {
                        padding: 10px;
                    }
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
                    input {
                      width: 35%;
                      padding: 12px 15px;
                      margin: 10px 0;
                      border: 2px solid #1abc9c;
                      border-radius: 4px;
                      font-size: 16px;
                      color: #34495e;
                      background-color: #f5f5dc;\s
                      transition: border-color 0.3s ease;
                    }
                
                    input:focus {
                      outline: none;
                      border-color: #16a085;\s
                      box-shadow: 0 0 5px rgba(22, 160, 133, 0.5);\s
                    }
                
                    input::placeholder {
                      color: #7f8c8d;\s
                      opacity: 1;\s
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