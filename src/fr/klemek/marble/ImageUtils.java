package fr.klemek.marble;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

final class ImageUtils {

    private static final int HEADER_SIZE = 54;

    private ImageUtils() {

    }

    static byte[] generateBmpFile(int width, byte[] data) {
        int height = data.length / (width * 3);

        data = fixBmpData(data, width, height);

        byte[] output = new byte[HEADER_SIZE + data.length];

        Utils.writeArray(output, getBmpHeader(data.length, width, height), 0);
        Utils.writeArray(output, data, HEADER_SIZE);

        return output;
    }

    private static byte[] fixBmpData(byte[] data, int width, int height) {
        if (data.length > height * width * 3)
            data = Utils.subArray(data, 0, height * width * 3);

        Utils.translateUnsigned(data);

        int linePadding = (width * 3) % 4;
        if (linePadding > 0) {
            byte[] tail = new byte[4 - linePadding];
            tail[tail.length - 1] = (byte) 255;
            data = Utils.interlaceArrays(data, tail, width * 3, height);
        }
        return data;
    }

    private static byte[] getBmpHeader(int dataSize, int width, int height) {
        byte[] header = new byte[HEADER_SIZE];


        Utils.writeArray(header, new byte[]{(int) 66, (int) 77}, 0); // BM
        Utils.writeArray(header, Utils.num2bytes(dataSize + HEADER_SIZE), 2); // file size
        //4 bytes application reserved
        Utils.writeArray(header, Utils.num2bytes(HEADER_SIZE), 10); // offset of data
        Utils.writeArray(header, Utils.num2bytes(40), 14); // real size of header
        Utils.writeArray(header, Utils.num2bytes(width), 18); //width
        Utils.writeArray(header, Utils.num2bytes(height), 22); //height
        Utils.writeArray(header, Utils.num2bytes(1, 2), 26); //color panes
        Utils.writeArray(header, Utils.num2bytes(24, 2), 28); //color depth
        //4 bytes compression method
        //4 bytes optional image size
        Utils.writeArray(header, Utils.num2bytes(3780), 38); //horizontal resolution
        Utils.writeArray(header, Utils.num2bytes(3780), 42); //vertical resolution
        //4 bytes number of colors
        //4 bytes number of important colors
        return header;
    }

    static boolean saveBmpFile(byte[] data, File bmpFile) {
        try (FileOutputStream fos = new FileOutputStream(bmpFile.getPath())) {
            fos.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean convertBmpToJpg(File bmpFile, File jpgFile) {
        try {
            BufferedImage inputImage = ImageIO.read(bmpFile);
            ImageIO.write(inputImage, "JPG", jpgFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
