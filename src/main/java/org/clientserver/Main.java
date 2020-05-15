package org.clientserver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        MessageCipher mc = new MessageCipher();
        final byte[] respond = Encode.buildResponse(mc);
        final String resp = new String(respond);
        System.out.println("Package to send: "+resp);
       System.out.println("\nResponse from server:");
       Decode.decode(Encode.buildResponse(mc),mc);
    }
}