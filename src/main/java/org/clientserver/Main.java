package org.clientserver;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws DecoderException {
        final String my_message = "my first text !!";
        final byte[] messageToBytes = my_message.getBytes(StandardCharsets.UTF_8);

        final String input_msg = ("13 01 0000000000000001 00000010 5E2C" + Hex.encodeHexString(messageToBytes) + " 4839").replace(" ","");
        encode(Hex.decodeHex(input_msg));

        System.out.println("Response from server:");
        encode(buildRepsonse());
    }

    public static void encode(final byte[] input_msg) {
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