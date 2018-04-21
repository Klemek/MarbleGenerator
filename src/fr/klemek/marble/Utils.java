package fr.klemek.marble;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

final class Utils {

    private Utils() {

    }

    /*
     * byte utils
     */

    static byte randomByte(Random rand) {
        return (byte) (rand.nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE);
    }

    static byte bound(int value) {
        if (value > Byte.MAX_VALUE)
            return Byte.MAX_VALUE;
        if (value < Byte.MIN_VALUE)
            return Byte.MIN_VALUE;
        return (byte) value;
    }

    /*
     * byte array utils
     */

    static byte[] subArray(byte[] array, int start, int stop) {
        byte[] out = new byte[stop - start];
        for (int i = start; i < stop; i++) {
            out[i - start] = array[i];
        }
        return out;
    }

    static void translateUnsigned(byte[] data){
        for(int i = 0; i < data.length; i++){
            data[i] = (byte) (data[i] - Byte.MIN_VALUE);
        }
    }

    static byte[] interlaceArrays(byte[] data, byte[] added, int size, int times){
        byte[] out = new byte[data.length + added.length*times];
        int size2 = size+added.length;
        for(int i = 0; i < times; i++){
            writeArray(out, data, size2*i, size2*i+size, size*i);
            writeArray(out, added, size2*i+size, size2*(i+1), 0);
        }
        writeArray(out, data, size2*times, out.length-size2*times, size*times);
        return out;
    }

    static void writeArray(byte[] out, byte[] data, int start, int stop, int padding){
        for(int i = 0; i < Math.min(stop, out.length)-start; i++){
            out[i+start] = data[i+padding];
        }
    }

    static void writeArray(byte[] out, byte[] data, int start, int padding){
        writeArray(out, data, start, start+data.length-padding, padding);
    }

    static void writeArray(byte[] out, byte[] data, int start){
        writeArray(out, data, start, 0);
    }

    static byte[] num2bytes(int number, int nbyte){
        byte[] b = new byte[nbyte];
        for(int i = 0; i < nbyte; i++){
            b[i] = (byte)(number%256);
            number = number/256;
        }
        return b;
    }

    static byte[] num2bytes(int number){
        return num2bytes(number, 4);
    }

    static int randInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(max-min)+min;
    }

    static int div(Random rand, byte src){
        return Math.round(rand.nextFloat()*2*src-src);
    }
}
