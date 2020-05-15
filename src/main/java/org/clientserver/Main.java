package org.clientserver;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, DecoderException {
        MessageCipher mc = new MessageCipher();
        final byte[] respond = Encode.buildResponse(mc);
        final String resp = new String(respond);
        System.out.println("Package to send: "+resp);
        System.out.println("\nResponse from server:");
        Decode.decode(Encode.buildResponse(mc),mc);
    }
}