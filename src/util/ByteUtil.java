package util;

import java.nio.ByteBuffer;

/**
 * Created by cwen on 17-2-17.
 */

public class ByteUtil {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] intToBytes(int x) {
        buffer.putInt(0, x);
        return buffer.array();
    }

    public static long bytesToInt(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getInt();
    }
}