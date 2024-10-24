package com.navi.captchaapi.data;

import com.navi.captchaapi.model.Captcha;
import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.db.*;
import com.navi.captchaapi.parser_lexer.db.objs.*;

import java.util.*;

public class CaptchaDAO {
    public List<Captcha> select(){
        return Connection.connectDB().captchas;
    }
    public Captcha viewCaptcha(String id){
        for(Captcha user : select()){
            if(user.getId().equals(id)) return user;
        }
        return null;
    }

    public void insertCaptcha(Captcha captcha){
        boolean valid = true;
        DBParser parser = Connection.connectDB();
        ArrayList<String> ids = parser.ids;
        Pos finalPos = parser.finalPos;

        for(String i : ids){
            if(captcha.getId().equals(i)){
                valid = false;
                break;
            }
        }
        if(valid){
            if(captcha.getId().isEmpty()) captcha.setId("captchaCC__" + (ids.size() + 1));
            if(captcha.getName().isEmpty()) captcha.setName("captchaCC__" + (ids.size() + 1));
            int position = Connection.calculatePosition(Connection.text, finalPos.getLine1(), finalPos.getCol1());
            String insertText = captcha.dbString() + "\n";
            if(!ids.isEmpty()) insertText = ",\n"+insertText;

            Connection.insertTextUser(position, insertText);
        }
        else{
            ErrorsLP.logError(new Location(0, 0), "Captcha ya existente (" + captcha.getId() + ")");
            System.out.println("Captcha ya existente");
        }
    }
    public void updateUser(Captcha captcha){
        boolean valid = false;
        DBParser parser = Connection.connectDB();
        ArrayList<Pos> positions = parser.positions;
        int pos = 0;

        for(Captcha i : parser.captchas){
            if(i.getId().equals(captcha.getId())){
                valid = true;
                break;
            }
            pos++;
        }

        if(valid){
            Pos position = positions.get(pos);
            int startPosition = Connection.calculatePosition(Connection.text, position.getLine1(), position.getCol1());
            int endPosition = Connection.calculatePosition(Connection.text, position.getLine2(), position.getCol2());

            Connection.updateTextUser(startPosition, endPosition+1, captcha.dbString());
        }
        else{
            ErrorsLP.logError(new Location(0, 0), "Captcha no encontrado (" + captcha.getId() + ")");
            System.out.println("Captcha no encontrado");
        }
    }

    public void deleteCaptcha(String id){
        boolean valid = false;
        DBParser parser = Connection.connectDB();
        ArrayList<String> ids = parser.ids;
        ArrayList<Pos> positions = parser.positions;
        int pos = 0;

        for(String u : ids){
            if(u.equals(id)){
                valid = true;
                break;
            }
            pos++;
        }
        if(valid){
            Pos position = positions.get(pos);
            int startPosition = Connection.calculatePosition(Connection.text, position.getLine1(), position.getCol1());
            int endPosition = Connection.calculatePosition(Connection.text, position.getLine2(), position.getCol2());
            if(Connection.text.charAt(startPosition-2) == ','){
                Connection.deleteTextUser(startPosition-3, endPosition+1);
            }
            else {
                if(Connection.text.charAt(endPosition+2) == ','){
                    Connection.deleteTextUser(startPosition-1, endPosition+3);
                }
                else{
                    Connection.deleteTextUser(startPosition-1, endPosition+1);
                }
            }
        }
        else{
            ErrorsLP.logError(new Location(0, 0), "Captcha no encontrado (" + id + ")");
            System.out.println("Captchas no encontrado");
        }
    }

    public Captcha parse(String text){
        return Connection.parse(text);
    }
}
