package com.navi.captchaapi.controller;

import com.navi.captchaapi.data.CaptchaDAO;
import com.navi.captchaapi.model.Captcha;
import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.TError;
import com.navi.captchaapi.parser_lexer.cc.Compile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/captchas/*")
public class CaptchaController extends HttpServlet {
    private final CaptchaDAO captchaDAO = new CaptchaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            var captchas = captchaDAO.select();

            String txt = "[\n";
            for(Captcha c: captchas){
                txt += c.dbString() + ",\n";
            }
            if(!captchas.isEmpty()) txt = txt.substring(0, txt.length() - 2);
            txt += "]";
            response.setStatus(HttpServletResponse.SC_OK);
            send(response, txt);
            return;
        }

        String[] splits = pathInfo.split("/");
        if(splits.length != 2){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String captchaId = splits[1];

        if(captchaDAO.viewCaptcha(captchaId) == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        var htmlDecode = captchaDAO.viewCaptcha(captchaId).getDecodedHtml();
        send(response, htmlDecode);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            readToInsert(request);
            if(ErrorsLP.getErrors().isEmpty()){
                var parser = Compile.parser;
                var label = Compile.parser.label;
                String staticVars = "";
                for(String var : parser.staticVariables){
                    staticVars += var + ";\n";
                }
                    String script = "<script>\n" + staticVars + label.functions() + Compile.parser.program.getScript() + "</script>";
                String html = label.toHtml(new StringBuilder(script));
                var newCaptcha = new Captcha(label.getId(), label.getName(), html);
                captchaDAO.insertCaptcha(newCaptcha);
                send(response, "Captcha creado exitosamente: " + label.getId());
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
            else {
                String err = "";
                for(TError e: ErrorsLP.getErrors()){
                    err += e.toString() + "\n";
                }
                send(response, err);
            }
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if(splits.length > 2){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String captchaId = splits[1];

        if(captchaDAO.viewCaptcha(captchaId) == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        var captcha = read(request);
        if(captcha != null){
            captchaDAO.updateUser(captcha);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void send(HttpServletResponse response, String res) throws ServletException, IOException {
        response.setContentType("text/plain");

        var out = response.getWriter();
        out.print(res);
    }
    public void readToInsert(HttpServletRequest request) throws ServletException, IOException {
        var buffer = new StringBuilder();
        var reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null) buffer.append(line).append("\n");

        String payload = buffer.toString();

        Compile.compile(payload);
    }
    public Captcha read(HttpServletRequest request) throws ServletException, IOException {
        var buffer = new StringBuilder();
        var reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null) buffer.append(line);

        String payload = "db.captchas(\n" + buffer + "\n)";
        return captchaDAO.parse(payload);
    }
}
