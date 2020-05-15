package org.clientserver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.ByteBuffer;

public class Encode {
    public static byte[] buildResponse(MessageCipher mc) throws BadPaddingException, IllegalBlockSizeException {
        final String message = "server response1";
        final byte[] my_message = mc.encipher(message);

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
