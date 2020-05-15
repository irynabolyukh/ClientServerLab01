package org.clientserver;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Decode {
    public static void decode(final String msg) throws DecoderException {
        final byte[] input_msg = Hex.decodeHex(msg);
        if(input_msg[0] != 0x13){
            throw new IllegalArgumentException("Invalid magic byte!");
        }
        final int userId = input_msg[1] & 0xFF;
        System.out.println("User ID: " + userId);
        final long packId = ByteBuffer.wrap(input_msg, 2, 8)
                .order(ByteOrder.BIG_ENDIAN)
                .getLong();
        System.out.println("Packet ID: " + packId);
        final int msgLen = ByteBuffer.wrap(input_msg, 10, 4)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        System.out.println("Length of message: " + msgLen);
        final short crc1 = ByteBuffer.wrap(input_msg, 14, 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();
        System.out.println("CRC 1: " + crc1);
        final short crc1Evaluated = CRC16.evaluateCrc(input_msg, 0, 14);
        if(crc1Evaluated != crc1){
            throw new IllegalArgumentException("CRC1 expected: " + crc1Evaluated + ", out was " + crc1);
        }
        byte[] message = new byte[msgLen];
        System.arraycopy(input_msg, 16, message, 0, msgLen);
        //decipher(message);
        final int cType = ByteBuffer.wrap(message, 0, 4)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        System.out.println("Command type: " + cType);
        final int mUserId = ByteBuffer.wrap(message, 4,4)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        System.out.println("Message owner: " + mUserId);
        byte[] jsonArray = new byte[msgLen-8];
        System.arraycopy(message, 8, jsonArray, 0, msgLen-8);
        System.out.println("Message body: " + new String(jsonArray));
        System.out.println("Message from user: " + new String(message));
        final short crc2 = ByteBuffer.wrap(input_msg, 16 + msgLen, 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();
        System.out.println("CRC 2: " + crc2);
        final short crc2Evaluated = CRC16.evaluateCrc(message, 0, msgLen);
        if(crc2Evaluated != crc2){
            throw new IllegalArgumentException("CRC2 expected: " + crc2Evaluated + ", out was " + crc2);
        }
    }
}
