package fr.klemek.marble;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Generator {

    private final int width;
    private final int height;
    private final int width2;
    private final int height2;
    private final int size;

    private final float slope;
    private final Color source;
    private final Color divergence;

    private long seed = 0L;

    private Color[][] table;
    private Random rand;

    Generator(int width, int height) {
        this(width, height, Utils.randInt(3, 12));
    }

    Generator(int width, int height, int size) {
        this(width, height, size, (float) Utils.randInt(40, 60) / 100f);
    }

    Generator(int width, int height, int size, float slope) {
        this(width, height, size, slope, new Color(Utils.randInt(0, 30),
                Utils.randInt(0, 30),
                Utils.randInt(0, 30)));
    }

    Generator(int width, int height, int size, float slope, Color divergence) {
        this(width, height, size, slope, divergence, null);
    }

    Generator(int width, int height, int size, float slope, Color divergence, Color source) {
        this.width = width;
        this.height = height;
        this.size = size;
        this.width2 = width % size == 0 ? (width / size) : (width / size) + 1;
        this.height2 = height % size == 0 ? (height / size) : (height / size) + 1;
        this.slope = slope;
        this.divergence = divergence;
        this.source = source;
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

    void generate() {
        if (seed == 0L)
            seed = ThreadLocalRandom.current().nextLong();
        System.out.println("\t\tseed : " + seed);
        System.out.println("\t\tsize : " + size);
        System.out.println("\t\tslope : " + slope);
        System.out.println("\t\tdivergence : " + divergence + " = " + divergence.sum());
        rand = new Random(seed);
        table = new Color[width2][height2];
        table[0][0] = source == null ? Color.random(rand) : source;
        System.out.println("\t\tsource : " + source);
        for (int y = 0; y < height2; y++)
            generateLine(y);
    }

    private void generateLine(int y) {
        for (int x = 0; x < width2; x++) {
            Color div = divergence.diverge(rand);
            if (x > 0 && y == 0) {
                table[x][y] = Color.add(table[x - 1][y], div);
            } else if (x == 0 && y > 0) {
                table[x][y] = Color.add(table[x][y - 1], div);
            } else if (x > 0 && y > 0) {
                table[x][y] = Color.add(new float[]{slope, 1f - slope, 1f}, new Color[]{table[x][y - 1], table[x - 1][y], div});
            }
        }
    }

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


}

