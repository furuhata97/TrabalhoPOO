/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author gusta
 */
public class GeraDatas {
    
    
    public static Date geraDataString(String data) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        return new Date(format.parse(data).getTime());
    }

    public static String geraDataDate(Date data) {
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }
}
