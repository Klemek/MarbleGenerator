package fr.klemek.marble;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

class WallpaperGenerator {

    public static void main(String[] args) {

        Logger.init("logging.properties");

        String file = "wallpaper";
        Dimension screen = getScreenSizes();
        int width = screen.width;
        int height = screen.height;
        int size = 0;

        if (args.length > 0) {
            file = args[0];
        }
        if (args.length > 1) {
            width = Integer.parseInt(args[1]);
            height = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            height = Integer.parseInt(args[2]);
        }
        if (args.length > 3) {
            size = Integer.parseInt(args[3]);
        }
        makeWallpaper(file, width, height, size);
    }

    private static Dimension getScreenSizes() {
        Dimension dim = new Dimension(0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            dim.width = Math.max(dim.width, gd.getDisplayMode().getWidth());
            dim.height = Math.max(dim.height, gd.getDisplayMode().getHeight());
        }
        return dim;
    }

    private static void makeWallpaper(String name, int width, int height, int size) {
        Logger.log(Level.INFO, "Making wallpaper '{0}' {1}x{2}px", name, width, height);

        File bmpFile = new File(name + ".bmp");
        File outputFile = new File(name + ".jpg");

        long t0 = System.currentTimeMillis();
        DefaultGenerator gen = size == 0 ? new DefaultGenerator(width, height) : new DefaultGenerator(width, height, size);
        long t1 = System.currentTimeMillis();
        gen.generate();
        Logger.log(Level.INFO, "\tGeneration done in {0} ms", (System.currentTimeMillis() - t1));

        t1 = System.currentTimeMillis();
        byte[] file = ImageUtils.generateBmpFile(width, gen.getData());
        Logger.log(Level.INFO, "\t\tData : {0} bytes", file.length);
        Logger.log(Level.INFO, "\tData writing done in {0} ms", (System.currentTimeMillis() - t1));

        t1 = System.currentTimeMillis();
        if (!ImageUtils.saveBmpFile(file, bmpFile))
            return;
        Logger.log(Level.INFO, "\tFile '{0}' writing done in {1} ms", bmpFile, (System.currentTimeMillis() - t1));

        t1 = System.currentTimeMillis();
        if (!ImageUtils.convertBmpToJpg(bmpFile, outputFile))
            return;
        Logger.log(Level.INFO, "\tFile '{0}' conversion to '{1}' done in {2} ms", bmpFile, outputFile, (System.currentTimeMillis() - t1));

        try {
            Files.delete(bmpFile.toPath());
            Logger.log(Level.INFO, "\tFile '{0}' deleted", bmpFile);
        } catch (IOException e) {
            Logger.logError(e);
        }

        Logger.log(Level.INFO, "Wallpaper done in {0} ms", (System.currentTimeMillis() - t0));
    }
}
