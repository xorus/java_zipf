package tp;

import java.text.Normalizer;
import java.util.*;

/**
 * Created by xorus on 30/05/16.
 */
public class WordTool {
    private static String accents = "àáâãäåòóôõöøèéêëçìíîïùúûüÿñ";
    private static String unAccented = "aaaaaaooooooeeeeciiiiuuuuyn";

    public static String cleanLine(String text) {
        // normaliser les caractères accentués (qu'ils aient les mêmes représentations utf8), mettre en minuscules,
        // convertir les apostrophes en quotes
        text = Normalizer.normalize(text, Normalizer.Form.NFD).toLowerCase().replaceAll("[’]", "'");

        for (Integer i = 0; i < accents.length(); i++) {
            text = text.replace(accents.charAt(i), unAccented.charAt(i));
        }

        return text;
    }

    public static String cleanWord(String word) {
        word = word.replaceAll("[œ]", "oe")
                .replaceAll("([^\\w']|[0-9])", ""); // tout ce qui n'est pas une lettre, ça dégage!

        // d' = de, l' = le, j' = je, ..... (pas terrible mais c'est déjà ça)
        if (word.length() == 1 && word.matches("[cdjlmnst]")) {
            return word.charAt(0) + "e";
        }

        return word;
    }

    /**
     * Pseudo stemmer qui marche plus ou moins pour les verbes du premier groupes et quelques cas particuliers
     *
     * @param words
     */
    public static String pseudoStemWord(String word) {
        String stemmedWord = word.replaceFirst("(cien|ciens|ciennes|iens|iennes)$", "e")
                .replaceFirst("(ues|uent|ua|uaient)$", "uer")
                .replaceFirst("(ie|ies|ient|irent)$", "ir")
                .replaceFirst("(logie|logies)$", "log")
                .replaceFirst("(usion|ution|usions|utions)$", "u")
                .replaceFirst("(ait|aient)$", "er")
                .replaceFirst("(amment)$", "ant")
                .replaceFirst("(emment)$", "ent")
                .replaceFirst("(eaux)$", "") // voir en dessous
                .replaceFirst("(aux)$", "al") // un cheval, des chevaux, mais pas un cerval des cerveaux!
                .replaceFirst("(es|ent|ait|aient)$", "");
        return stemmedWord;
    }

    public static void pseudoStemWordCount(Map<String, Integer> words) {
        Map<String, List<String>> stemmed = new TreeMap<>();

        Iterator<String> it = words.keySet().iterator();
        while (it.hasNext()) {
            String originalWord = it.next();
            // si le mot fait plus de 5 lettres pour éviter les problèmes
            if (originalWord.length() < 5) {
                continue;
            }
            String stemmedWord = pseudoStemWord(originalWord);

            // add stemmed word to the list
            if (stemmed.containsKey(stemmedWord)) {
                stemmed.get(stemmedWord).add(originalWord);
            } else {
                List list = new ArrayList<>();
                list.add(originalWord);
                stemmed.put(stemmedWord, list);
            }
        }

        // now we will merge/squash similar words we just found into our initial word matches / count
        // e.g. keeping only one word for ["journal", "journaux"] ==> "journal"
        for (Map.Entry<String, List<String>> entry : stemmed.entrySet()) {
            if (entry.getValue().size() > 1) {
                String firstWord = null;
                for (String word : entry.getValue()) {
                    // skip first
                    if (firstWord == null) {
                        firstWord = word;
                        continue;
                    }
                    // remove the word from the original matches and add it's value to the original word
//                    System.out.println("removing " + word + " from collection, collecting : "
//                            + words.get(firstWord) + " + " + words.get(word) + " points");
                    words.put(firstWord, words.get(firstWord) +
                            words.get(word));
                    words.remove(word);
                }
            }
        }
//        System.out.println(stemmed);
    }
}
