package com.navi.captchaapi.model;
import com.navi.captchaapi.parser_lexer.cc.obj.Parameter;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Captcha {
    private String id;
    private String name;
    private int timesUsed;
    private int successes;
    private int fails;
    private Date lastUse;
    private String html;

    public Captcha(String id, String name, String html) {
        this.id = id;
        this.name = name;
        this.html = html;
        this.timesUsed = 0;
        this.successes = 0;
        this.fails = 0;
        this.lastUse = null;
    }

    public String dbString(){
        String encodedHtml = Base64.getEncoder().encodeToString(html.getBytes());
        return "{\n" +
                "\t\"ID\":\"" + id + "\",\n"+
                "\t\"NOMBRE\":\"" + name + "\",\n"+
                "\t\"USADO\":" + timesUsed + ",\n"+
                "\t\"ACIERTOS\":" + successes + ",\n"+
                "\t\"FALLOS\":" + fails + ",\n"+
                "\t\"ULTIMO_USO\":\"" + dateToString(lastUse) + "\",\n" +
                "\t\"HTML\":\"" + encodedHtml + "\"\n"+
                "}"
                ;
    }
    public String getDecodedHtml(){
        byte[] decodedBytes = Base64.getDecoder().decode(html);
        return new String(decodedBytes);
    }

    public void setLastUseString(String date){
        this.lastUse = stringToDate(date);
    }

    private Date stringToDate(String date) {
        Date d;
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return d;
    }

    private String dateToString(Date date) {
        if (date == null) {
            return "null";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

}
