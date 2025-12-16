package searchengine.services;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String text = "Повторное появление леопарда в Осетии позволяет " +
                "предположить, что леопард постоянно обитает в" +
                " некоторых районах Северного Кавказа.";

        LemmaSearcher lemmaSearcher = new LemmaSearcher();
        lemmaSearcher.lemmas(text);

    }
}
