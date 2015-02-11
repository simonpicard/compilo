/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import java.nio.charset.Charset;


/**
 *
 * @author arnaud
 */
public class BaseEnvironment implements LlvmItem {

    @Override
    public byte[] toByte() {
        String res = "";
        return res.getBytes(Charset.forName("UTF-8"));
    }

}
