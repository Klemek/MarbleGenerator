package fr.klemek.marble;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

class DefaultGenerator extends Generator {

    private final float slope;
    private final Color source;
    private final Color divergence;

    DefaultGenerator(int width, int height) {
        this(width, height, Utils.randInt(3, 12));
    }

    DefaultGenerator(int width, int height, int size) {
        this(width, height, size, (float) Utils.randInt(40, 60) / 100f);
    }

    private DefaultGenerator(int width, int height, int size, float slope) {
        this(width, height, size, slope, new Color(Utils.randInt(0, 30),
                Utils.randInt(0, 30),
                Utils.randInt(0, 30)));
    }

    private DefaultGenerator(int width, int height, int size, float slope, Color divergence) {
        this(width, height, size, slope, divergence, null);
    }

    private DefaultGenerator(int width, int height, int size, float slope, Color divergence, Color source) {
        super(width, height, size);
        this.slope = slope;
        this.divergence = divergence;
        this.source = source;
    }

    @Override
    void generate() {
        if (seed == 0L)
            seed = ThreadLocalRandom.current().nextLong();
        Logger.log(Level.INFO, "Generating....");
        Logger.log(Level.INFO, "\tseed : {0}", seed);
        Logger.log(Level.INFO, "\tsize : {0}", size);
        Logger.log(Level.INFO, "\tslope : {0}", slope);
        Logger.log(Level.INFO, "\tdivergence : {0} = {1}", divergence, divergence.sum());
        rand = new Random(seed);
        table = new Color[width2][height2];
        table[0][0] = source == null ? Color.random(rand) : source;
        Logger.log(Level.INFO, "\tsource : {0}", source);
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

}

