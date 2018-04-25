package fr.klemek.marble;

import java.io.File;
import java.util.Random;

abstract class Generator {

    private final int width;
    private final int height;
    final int width2;
    final int height2;
    final int size;

    long seed = 0L;

    Color[][] table;
    Random rand;

    Generator(int width, int height, int size) {
        this.width = width;
        this.height = height;
        this.size = size;
        this.width2 = width % size == 0 ? (width / size) : (width / size) + 1;
        this.height2 = height % size == 0 ? (height / size) : (height / size) + 1;
    }

    public Color[][] getTable() {
        return table;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth2() {
        return width2;
    }

    public int getHeight2() {
        return height2;
    }

    public int getSize() {
        return size;
    }

    abstract void generate();

    byte[] getData() {
        byte[] data = new byte[width * height * 3];
        int k = 0;
        for (int y = 0; y < height2; y++) {
            for (int x = 0; x < width2; x++) {
                for (int j = 0; j < Math.min(size, width - x * size); j++) {
                    if (k >= data.length) {
                        System.err.println("\t\toverflow at x:" + x + " y:" + y + " j:" + j + " k:" + k);
                        return data;
                    }
                    data[k++] = table[x][y].r;
                    data[k++] = table[x][y].g;
                    data[k++] = table[x][y].b;
                }
            }
            for (int i = 0; i < Math.min(size, height - y * size) - 1; i++) {
                Utils.writeArray(data, data, k, k + width * 3, k - width * 3);
                k += width * 3;
            }
        }
        return data;
    }

    boolean saveBmp(File file) {
        byte[] fileData = ImageUtils.generateBmpFile(width, getData());
        return ImageUtils.saveBmpFile(fileData, file);
    }

    void show() {
        new MarbleViewer(this);
    }


}

