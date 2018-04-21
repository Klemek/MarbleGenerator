package fr.klemek.marble;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @org.junit.Test
    public void bound() {
        assertEquals((byte) -128, Utils.bound(-200));
        assertEquals((byte) -128, Utils.bound(-129));
        assertEquals((byte) -128, Utils.bound(-128));
        assertEquals((byte) -64, Utils.bound(-64));
        assertEquals((byte) 0, Utils.bound(0));
        assertEquals((byte) 64, Utils.bound(64));
        assertEquals((byte) 127, Utils.bound(127));
        assertEquals((byte) 127, Utils.bound(128));
        assertEquals((byte) 127, Utils.bound(200));
    }

    @org.junit.Test
    public void subArray() {
        byte[] array0 = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(new byte[]{2, 3, 4}, Utils.subArray(array0, 2, 5));
    }

    @org.junit.Test
    public void translateUnsigned() {
        byte[] array0 = new byte[]{-128, -64, 0, 64, 127};
        Utils.translateUnsigned(array0);
        assertArrayEquals(new byte[]{0, 64, (byte) 128, (byte) 192, (byte) 255}, array0);
    }

    @org.junit.Test
    public void interlaceArrays() {
        byte[] array0 = new byte[10];
        byte[] array1 = new byte[]{1, 1};
        assertArrayEquals(new byte[]{0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0}, Utils.interlaceArrays(array0, array1, 3, 2));
    }

    @org.junit.Test
    public void writeArray() {
        byte[] array0 = new byte[10];
        byte[] array1 = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utils.writeArray(array0, array1, 2, 5, 3);
        assertArrayEquals(new byte[]{0, 0, 3, 4, 5, 0, 0, 0, 0, 0}, array0);
    }

    @org.junit.Test
    public void writeArray1() {
        byte[] array0 = new byte[10];
        byte[] array1 = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utils.writeArray(array0, array1, 2, 3);
        assertArrayEquals(new byte[]{0, 0, 3, 4, 5, 6, 7, 8, 9, 0}, array0);
    }

    @org.junit.Test
    public void writeArray2() {
        byte[] array0 = new byte[10];
        byte[] array1 = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utils.writeArray(array0, array1, 2);
        assertArrayEquals(new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7}, array0);
    }

}