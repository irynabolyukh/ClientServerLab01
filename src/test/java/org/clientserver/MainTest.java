package org.clientserver;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest{

//    @Test
//    void checkWhether_ValidInputMessage() throws DecoderException, BadPaddingException, IllegalBlockSizeException {
//        final String message = "my first text !!";
//        final byte[] messageToBytes = message.getBytes(StandardCharsets.UTF_8);
//        final String input_msg = ("13 01 0000000000000001 00000010 5E2C" + Hex.encodeHexString(messageToBytes) + " 4839").replace(" ","");
//
//        Decode.decode(Hex.decodeHex(input_msg));
//    }
//
//    @Test
//    void checkWhether_InvalidMagicByte() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> Decode.decode(Hex.decodeHex("11"))
//        );
//    }

    @Test
    void checkWhether_InvalidCrc1(){

    }

    @Test
    void checkWhether_InvalidCrc2(){

    }
}