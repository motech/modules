package org.motechproject.transliteration.mapping;

import java.util.HashMap;

/**
 * Created by kosh on 3/13/14.
 */
// Class defines the mappings in english for devanagiri script based on the ITRANS encoding
public class ItransAsciiMapping implements CharacterMapping {

    private static String[] vowels = "a A i I u U RRi RRI LLi LLI  e ai  o au".split(" ");

    private static String[] otherMarks = "M H .N".split(" ");

    private static String[] virama = "".split(" ");

    private static String[] consonants = "k kh g gh ~N ch Ch j jh ~n T Th D Dh N t th d dh n p ph b bh m y r l v sh Sh s h L kSh j~n".split(" ");

    private static String[] symbols = "0 1 2 3 4 5 6 7 8 9 OM .a | ||".split(" ");

    private static String[] candra = ".c".split(" ");

    private static String[] zwj = "".split(" ");

    private static String[] skip = "_".split(" ");

    private static String[] accent = "\\' \\_".split(" ");

    private static String[] comboAccent = "\\'H \\_H \\'M \\_M".split(" ");

    private static String[] other = "q K G z .D .Dh f Y R".split(" ");

    private HashMap<String, String[]> mapping;

    public ItransAsciiMapping() {
        mapping = new HashMap<String, String[]>();
        mapping.put("vowels", vowels);
        mapping.put("otherMarks", otherMarks);
        mapping.put("consonants", consonants);
        mapping.put("symbols", symbols);
        mapping.put("candra", candra);
        mapping.put("zwj", zwj);
        mapping.put("skip", skip);
        mapping.put("accent", accent);
        mapping.put("comboAccent", comboAccent);
        mapping.put("other", other);
        mapping.put("virama", virama);
    }

    private HashMap<String, String[]> createAlternateMapping() {
        HashMap<String, String[]> alt = new HashMap<>();
        alt.put("A", new String[] { "aa" });
        alt.put("I", new String[] { "ii", "ee" });
        alt.put("U", new String[] { "uu", "oo" });
        alt.put("RRi", new String[] { "R^i" });
        alt.put("RRI", new String[] { "R^I" });
        alt.put("LLi", new String[] { "L^i" });
        alt.put("LLI", new String[] { "L^I" });
        alt.put("M", new String[] { ".m", ".n" });
        alt.put("~N", new String[] { "N^" });
        alt.put("ch", new String[] { "c" });
        alt.put("Ch", new String[] { "C", "chh" });
        alt.put("~n", new String[] { "JN" });
        alt.put("v", new String[] { "w" });
        alt.put("Sh", new String[] { "S", "shh" });
        alt.put("kSh", new String[] { "kS", "x" });
        alt.put("j~n", new String[] { "GY", "dny" });
        alt.put("OM", new String[] { "AUM" });
        alt.put("z", new String[] { "J" });
        alt.put(".a", new String[] { "~" });
        alt.put("|", new String[] { "." });
        alt.put("||", new String[] { ".." });
        alt.put("m", new String[] { "M" });
        return alt;
    }

    public HashMap<String, String[]> getMapping() {
        return this.mapping;
    }

    public HashMap<String, String[]> getAlternateMapping() {
        return createAlternateMapping();
    }
}
