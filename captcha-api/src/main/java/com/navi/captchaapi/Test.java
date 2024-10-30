package com.navi.captchaapi;

import com.navi.captchaapi.data.CaptchaDAO;
import com.navi.captchaapi.data.Connection;
import com.navi.captchaapi.model.Captcha;
import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.cc.Compile;
import com.navi.captchaapi.parser_lexer.cc.obj.StaticVariables;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.SymTableGlobalVisitor;

public class Test {
    public static void main(String[] args){
        String text = """
                <C_CC [id= "captcha_matematico_1"] [name= "Captcha Matemático 1"]> !! El encabezado de la página que tendrá mi captcha\s
                <C_HEAD>\s
                <C_LINK\s
                !! El link al que redirige mi captcha\s
                [href= "https://www.mclibre.org/consultar/htmlcss/html/html-etiquetas.html"]> </C_LINK>\s
                !! El título de mi página\s
                <C_TITLE> Mi primer Captcha Matemático</C_TITLE>\s
                </C_HEAD>\s
                !! El cuerpo de la página\s
                <C_BODY [background= "#e5e6ea"]>\s
                !! un título simple estilizado\s
                <C_H1 [id= "title_1"] [text-align= "center"] [color= "#7eff33"] > Mi primer Captcha Matemático\s
                </C_H1>\s
                !! Un salto normal\s
                <c_br>\s
                !! Información de la operación a resolver en el captcha\s
                <C_SPAM [id= "mostrar_1"] [text-align= "center"] [color= "#3366ff"] > ¿ Qué resultado genera la operación siguiente: 5+5 ?\s
                </C_SPAM>\s
                !! Input para la Respuesta del usuario generado con un scripting\s
                <C_SCRIPTING>\s
                ON_LOAD () [\s
                !!Estas instrucciones se ejecutan media vez se entra al scripting !! Insertamos el input con sus parámetros con la instrucción INSERT
                INSERT('<C_INPUT [type= "text"] [text-align= "center"]\s
                [id= "entrada_1"] ></C_INPUT>');\s
                ]\s
                </C_SCRIPTING>
                !! Boton que llama a la funcionalidad calc\s
                <C_BUTTON [id= "boton_1"] [onclick= "FUNCTION_calc()"] [background="green"]> Procesar...\s
                </C_BUTTON>\s
                !! Scripting para la función calc\s
                <C_SCRIPTING>\s
                FUNCTION_calc() [\s
                !! Estas instrucciones no se ejecutan hasta llamar a FUNCTION_calc()
                integer @global contador_fallas = 5;\s
                string result_caja_texto = getElemenById('entrada_1');\s
                string result = "10";\s
                string mensaje_fallo = "El captcha no fue validado intente otra vez ";\s
                string mensaje_acierto = "El captcha fue validado ";\s
                string mensaje_final = "El captcha no logró ser validado :( intente mas tarde";\s
                !! Validacion del numero de oportunidades restantes\s
                IF (contador_fallas == 0) THEN\s
                INIT {:\s
                ALERT_INFO(mensaje_final);\s
                EXIT();\s
                :} END\s
                !! Validación de fallas y aciertos\s
                IF (result_caja_texto == result ) THEN\s
                !!si el resultado es correcto\s
                INIT {:\s
                ALERT_INFO(mensaje_acierto);\s
                REDIRECT(); !!puede usarse también EXIT() para redirigir\s
                :} END\s
                ELSE\s
                !!si el intento es incorrecto\s
                INIT {:\s
                ALERT_INFO(mensaje_fallo);\s
                contador_fallas = contador_fallas -1;\s
                :} END\s
                ]\s
                </C_SCRIPTING>\s
                </C_BODY>\s
                </C_CC>
                """;

        Compile.compile(text);

        ErrorsLP.getErrors().forEach(System.out::println);

        if(ErrorsLP.getErrors().isEmpty()){
            var parser = Compile.parser;
            var label = Compile.parser.label;

            //var newCaptcha = new Captcha(label.getId(), label.getName(), html);
            //var newCaptcha = new Captcha("", "", html);

            //captchaDAO.insertCaptcha(newCaptcha);
            //System.out.println(newCaptcha.getHtml());

            var program = parser.program;
            var globalSymT = new SymTableGlobalVisitor("xd");
            globalSymT.setGlobal(program.getTable());

            globalSymT.visit(program);
            ErrorsLP.getErrors().forEach(System.out::println);

            /*String script = "<script>\nvar globalVariables = [];\n" + StaticVariables.getVariables() + label.functions() + program.getScript() + "</script>";
            String html = label.toHtml(new StringBuilder(script));
            System.out.println(script);*/
        }

    }

}
