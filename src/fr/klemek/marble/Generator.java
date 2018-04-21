package fr.klemek.marble;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Generator {

    private final int width;
    private final int height;
    private final int width2;
    private final int height2;
    private final int size;

    private float slope;
    private Color source;
    private Color divergence;

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

    Generator(int width, int height, int size, float slope, Color divergence){
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

    private static Color getDivergence(int width, int height, int size) {
        int size2 = (int) Math.min(Math.max(width / size, height / size), Byte.MAX_VALUE * 1.5f);
        Color c;
        do {
            c = new Color(Utils.randInt(0, Byte.MAX_VALUE),
                    Utils.randInt(0, Byte.MAX_VALUE),
                    Utils.randInt(0, Byte.MAX_VALUE));
        } while (c.sum() * 2 < size2 || c.sum() > Byte.MAX_VALUE * 2);
        return c;
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

    void inspect(int x, int y, int size, boolean unsigned) {

        System.out.println(String.format("Inspect area : %d-%d x %d-%d", x, x + size, y, y + size));

        int sumr = 0;
        int sumg = 0;
        int sumb = 0;
        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                System.out.print(String.format("%1$-12s %2$-4d ", table[i][j].toString(unsigned), table[i][j].sum()));
                sumr += unsigned ? table[i][j].r - Byte.MIN_VALUE : table[i][j].r;
                sumg += unsigned ? table[i][j].g - Byte.MIN_VALUE : table[i][j].g;
                sumb += unsigned ? table[i][j].b - Byte.MIN_VALUE : table[i][j].b;
            }
            System.out.println();
        }

        System.out.println(String.format("mean : (%d,%d,%d) %d", sumr / (size * size), sumg / (size * size), sumb / (size * size), (sumr + sumg + sumb) / (size * size)));
    }

    void inspectDivergence(int x0, int y0, int size) {
        System.out.println(String.format("Inspect divergence in area : %d-%d x %d-%d", x0, x0 + size, y0, y0 + size));

        int sumr = 0;
        int sumg = 0;
        int sumb = 0;
        for (int x = x0; x < x0 + size; x++) {
            for (int y = y0; y < y0 + size; y++) {

                Color div = table[x][y];

                if (x > 0 && y == 0) {
                    div = Color.add(new float[]{1f,-1f},new Color[]{table[x][y], table[x - 1][y]});
                } else if (x == 0 && y > 0) {
                    div = Color.add(new float[]{1f,-1f},new Color[]{table[x][y], table[x][y-1]});
                } else if (x > 0 && y > 0) {
                    div = Color.add(new float[]{1f,-slope,slope-1f},new Color[]{table[x][y], table[x][y - 1], table[x - 1][y]});
                }

                System.out.print(String.format("%1$-12s %2$-4d ", div, div.sum()));

                sumr += div.r;
                sumg += div.g;
                sumb += div.b;
            }
            System.out.println();
        }
        System.out.println(String.format("mean : (%d,%d,%d) %d", sumr / (size * size), sumg / (size * size), sumb / (size * size), (sumr + sumg + sumb) / (size * size)));
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

