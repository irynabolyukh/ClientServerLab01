package org.clientserver;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Decode {
    public static void decode(final byte[] input_msg, MessageCipher mc) throws BadPaddingException, IllegalBlockSizeException {
        if(input_msg[0] != 0x13){
            throw new IllegalArgumentException("Invalid magic byte!");
        }
        final int userId = input_msg[1] & 0xFF;
        System.out.println("User ID: " + userId);
        final long packId = ByteBuffer.wrap(input_msg, 2, 8)
                .order(ByteOrder.BIG_ENDIAN)
                .getLong();
        System.out.println("Packet ID: " + packId);
        final int msgLenEnc = ByteBuffer.wrap(input_msg, 10, 4)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        System.out.println("Length of message: " + msgLenEnc);
        final short crc1 = ByteBuffer.wrap(input_msg, 14, 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();
        System.out.println("CRC 1: " + crc1);
        final short crc1Evaluated = CRC16.evaluateCrc(input_msg, 0, 14);
        if(crc1Evaluated != crc1){
            throw new IllegalArgumentException("CRC1 expected: " + crc1Evaluated + ", out was " + crc1);
        }
        byte[] enc_message = new byte[msgLenEnc];
        System.arraycopy(input_msg, 16, enc_message, 0, msgLenEnc);
        byte[] decr_message = mc.decipher(enc_message);
        final int msgLenDecr = decr_message.length;
        System.out.println("Whole message: " + new String(decr_message));
        final int cType = ByteBuffer.wrap(decr_message, 0, 4)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        System.out.println("Command type: " + cType);
        final int mUserId = ByteBuffer.wrap(decr_message, 4,4)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        System.out.println("Message owner: " + mUserId);
        byte[] jsonArray = new byte[msgLenDecr-8];
        System.arraycopy(decr_message, 8, jsonArray, 0, msgLenDecr-8);
        System.out.println("Message body: " + new String(jsonArray));
        final short crc2 = ByteBuffer.wrap(input_msg, 16 + msgLenEnc, 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();
        System.out.println("CRC 2: " + crc2);
        final short crc2Evaluated = CRC16.evaluateCrc(enc_message, 0, msgLenEnc);
        if(crc2Evaluated != crc2){
            throw new IllegalArgumentException("CRC2 expected: " + crc2Evaluated + ", out was " + crc2);
        }
    }
}
