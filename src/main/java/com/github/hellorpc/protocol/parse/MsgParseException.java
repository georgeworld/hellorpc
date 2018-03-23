/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.hellorpc.protocol.parse;

/**
 *
 * @author dev1
 */
public class MsgParseException extends RuntimeException{

    public MsgParseException() {
    }

    public MsgParseException(String message) {
        super(message);
    }

    public MsgParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MsgParseException(Throwable cause) {
        super(cause);
    }

    public MsgParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
    
}
