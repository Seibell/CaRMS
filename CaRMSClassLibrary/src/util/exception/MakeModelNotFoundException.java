/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author ryanl
 */
public class MakeModelNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>MakeModelNotFoundException</code> without
     * detail message.
     */
    public MakeModelNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MakeModelNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public MakeModelNotFoundException(String msg) {
        super(msg);
    }
}
