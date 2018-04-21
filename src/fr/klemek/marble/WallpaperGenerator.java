package fr.klemek.marble;

import java.awt.*;
import java.io.File;

class WallpaperGenerator {

    public static void main(String[] args) {

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
        System.out.println("Making wallpaper '" + name + "' " + width + "x" + height + "px");

        File bmpFile = new File(name + ".bmp");
        File outputFile = new File(name + ".jpg");

        long t0 = System.currentTimeMillis();
        Generator gen = size == 0 ? new Generator(width, height) : new Generator(width, height, size);
        long t1 = System.currentTimeMillis();
        gen.generate();
        System.out.println("\tGeneration done in " + (System.currentTimeMillis() - t1) + " ms");

        t1 = System.currentTimeMillis();
        byte[] file = ImageUtils.generateBmpFile(width, gen.getData());
        System.out.println("\t\t" + file.length + " bytes");
        System.out.println("\tData writing done in " + (System.currentTimeMillis() - t1) + " ms");

        t1 = System.currentTimeMillis();
        if (!ImageUtils.saveBmpFile(file, bmpFile))
            return;
        System.out.println("\tFile writing done in " + (System.currentTimeMillis() - t1) + " ms");

        t1 = System.currentTimeMillis();
        if (!ImageUtils.convertBmpToJpg(bmpFile, outputFile))
            return;
        System.out.println("\tFile converting done in " + (System.currentTimeMillis() - t1) + " ms");

        if (bmpFile.delete())
            System.out.println("\tFile '" + bmpFile.getName() + "' deleted");

        System.out.println("Wallpaper done in " + (System.currentTimeMillis() - t0) + " ms");
    }
}
