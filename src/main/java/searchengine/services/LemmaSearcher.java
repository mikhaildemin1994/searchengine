package searchengine.services;

import lombok.SneakyThrows;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.util.*;


public class LemmaSearcher {
    private static final String WORD_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
    private static final String PUNCTUATION_REGEX = "\\p{Punct}";
    private static final String[] particles = new String[]{"МЕЖД","ПРЕДЛ","СОЮЗ"};
    private final HashMap<String, Integer> lemmaMap = new HashMap<>();

    @SneakyThrows
    public Map<String, Integer> lemmas(String text) {
        List<String> words = new ArrayList<>();
        LuceneMorphology luceneMorph =
                new RussianLuceneMorphology();

        String[] wordsMassive = text.split("\\s");

        for(String word : wordsMassive) {
            if (!word.contains(WORD_REGEX)) {
                words.add(word.toLowerCase().replaceAll(PUNCTUATION_REGEX, ""));
            }
        }

        for(String word : words) {
            if(!lemmaMap.containsKey(luceneMorph.getNormalForms(word))) {
                lemmaMap.put(word,
                        luceneMorph.getNormalForms(word).size());
            }
        }

        System.out.println(lemmaMap);
        return lemmaMap;
    }
}
