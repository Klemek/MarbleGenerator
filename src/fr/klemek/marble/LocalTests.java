package fr.klemek.marble;

class LocalTests {

    public static void main(String[] args) {
        Generator gen = new Generator(800, 800, 2);
        gen.generate();

        new MarbleViewer(gen);
    }

}
