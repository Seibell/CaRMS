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
public class DispatchNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DispatchNotFoundException</code> without
     * detail message.
     */
    public DispatchNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DispatchNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DispatchNotFoundException(String msg) {
        super(msg);
    }
}
