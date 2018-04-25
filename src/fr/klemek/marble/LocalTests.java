package fr.klemek.marble;

import java.io.File;

class LocalTests {

    public static void main(String[] args) {
        Logger.init("logging.properties");
        DefaultGenerator gen = new DefaultGenerator(800, 800, 2);
        gen.generate();
        gen.saveBmp(new File("temp.bmp"));
        gen.show();
    }

}
