package org.motechproject.transliteration.mapping;

import org.motechproject.transliteration.mapping.CharacterMapping;

import java.util.HashMap;

/**
 * Created by kosh on 3/13/14.
 */
// Class defines the character mapping for Devanagiri script
public class HindiMapping implements CharacterMapping {
    private static String[] vowels = "अ आ इ ई उ ऊ ऋ ॠ ऌ ॡ ऎ ए ऐ ऒ ओ औ".split(" ");

    private static String[] vowelMarks = "ा ि ी ु ू ृ ॄ ॢ ॣ ॆ े ै ॊ ो ौ".split(" ");

    private static String[] otherMarks = "ं ः ँ".split(" ");

    private static String[] virama = "्".split(" ");

    private static String[] consonants = "क ख ग घ ङ च छ ज झ ञ ट ठ ड ढ ण त थ द ध न प फ ब भ म य र ल व श ष स ह ळ क्ष ज्ञ".split(" ");

    private static String[] symbols = "० १ २ ३ ४ ५ ६ ७ ८ ९ ॐ ऽ । ॥".split(" ");

    private static String[] zwj = new String[] { "\u200D" };

    private static String[] skip = "".split(" ");

    private static String[] accent = new String[] { "\u0951", "\u0952"};

    private static String[] comboAccent = "ः॑ ः॒ ं॑ ं॒".split(" ");

    private static String[] candra = "ॅ".split(" ");

    private static String[] other = "़क ़ख ़ग ़ज ़ड ़ढ ़फ ़य ़र"  .split(" ");

    private HashMap<String, String[]> mapping;

    public HindiMapping(){
        mapping = new HashMap<String, String[]>();
        mapping.put("vowels", vowels);
        mapping.put("vowelMarks", vowelMarks);
        mapping.put("otherMarks", otherMarks);
        mapping.put("virama", virama);
        mapping.put("consonants", consonants);
        mapping.put("symbols", symbols);
        mapping.put("zwj", zwj);
        mapping.put("skip", skip);
        mapping.put("accent", accent);
        mapping.put("comboAccent", comboAccent);
        mapping.put("candra", candra);
        mapping.put("other", other);
    }

    public HashMap<String, String[]> getMapping(){
        return this.mapping;
    }

    public HashMap<String, String[]> getAlternateMapping() {
        return new HashMap<>();
    }
}
