package org.clientserver;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws DecoderException {
        final String my_message = "my first text !!";
        final byte[] messageToBytes = my_message.getBytes(StandardCharsets.UTF_8);

        final String input_msg = ("13 01 0000000000000001 00000010 5E2C" + Hex.encodeHexString(messageToBytes) + " 4839").replace(" ","");
        Decode.decode(input_msg);

       // System.out.println("Response from server:");
       // Decode.decode(buildRepsonse());
    }



    public static byte[] buildRepsonse(){
        final byte[] my_message = "server response".getBytes(StandardCharsets.UTF_8);
        final byte[] header = new byte[]{
                0x13,
                0,
                0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0A,
                0x0, 0x0, 0x0, (byte) my_message.length
        };

        return ByteBuffer.allocate(14 + 2 + my_message.length + 2)
                .put(header)
                .putShort(CRC16.evaluateCrc(header, 0, header.length))
                .put(my_message)
                .putShort(CRC16.evaluateCrc(my_message, 0, my_message.length))
                .array();
    }



}