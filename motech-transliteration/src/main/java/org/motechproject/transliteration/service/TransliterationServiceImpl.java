package org.motechproject.transliteration.service;

import org.motechproject.transliteration.mapping.CharacterMapping;
import org.motechproject.transliteration.mapping.HindiMapping;
import org.motechproject.transliteration.mapping.ItransAsciiMapping;
import org.motechproject.transliteration.service.TransliterationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kosh on 3/14/14.
 */
@Service("TransliterationService")
public class TransliterationServiceImpl implements TransliterationService {

    // Transliterates a given string from English to Hindi using ITRANS encoding

    @Override
    public String transliterate(String data) {
        // Since ITRANS is a case-sensitive encoding, the best results would be achieved by
        // passing the data in all lower case. Here, we optimistically convert to lower case
        // as (first letter of) names are auto-converted to upper case in Roman script

        // NOTE:  The conversion to lower case results in a hybrid scheme between ITRANS and Velthuis.
        // While this might not be an ideal case, we consider this an acceptable risk since our data merge
        // takes place manually (read by humans), where the enunciation of the name/word needs to be similar
        // but not perfect.
        // TODO: Consider forcing the consumer of the module to call the API in lower case?
        String[] words = data.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();
        CharacterMapping englishMapping = new ItransAsciiMapping();
        CharacterMapping hindiMapping = new HindiMapping();
        for (String currentWord : words) {
            result.append(transliterateHelper(currentWord, englishMapping, hindiMapping));
            result.append(" ");
        }

        return result.toString().trim();
    }

    private String transliterateHelper(String data, CharacterMapping englishMapping, CharacterMapping hindiMapping) {
        int maxTokenLength = computeMaxTokenLength(englishMapping);
        int inputLength = data.length();
        boolean followsConsonant = false;
        StringBuilder buffer = new StringBuilder(inputLength);
        String tokenBuffer = "";

        HashMap<String, String> tokenMap = createTokenMap(englishMapping, hindiMapping);
        HashMap<String, String> vowelMap = createVowelMap(englishMapping, hindiMapping);
        HashMap<String, String[]> englishTokenGroups = englishMapping.getMapping();
        HashMap<String, String[]> hindiTokenGroups = hindiMapping.getMapping();

        // read the input char by char
        for(int i = 0; i < inputLength || !tokenBuffer.isEmpty(); i++) {

            // if we are still in the range of our input, add char to token buffer
            if (i < inputLength) {
                char currentChar = data.charAt(i);
                int difference = maxTokenLength - tokenBuffer.length();
                if(difference > 0 && i < inputLength) {
                    tokenBuffer += currentChar;
                    if (difference > 1) {
                        continue;
                    }
                }
            }

            // match token substrings. start from max token length in target language and work backwards
            for(int j = 0; j < maxTokenLength; j++) {
                int tokenLength = maxTokenLength - j;
                tokenLength = (tokenLength > tokenBuffer.length()) ? tokenBuffer.length() : tokenLength;
                String token = tokenBuffer.substring(0, tokenLength);
                String tempLetter = tokenMap.get(token);
                if (tempLetter != null) {
                    if (followsConsonant) {
                        String vowelMark = vowelMap.get(token);
                        if (vowelMark != null) {
                            buffer.insert(0, vowelMark);
                        } else if (!token.equals("a")) {
                            buffer.insert(0, hindiTokenGroups.get("virama")[0]);
                            buffer.insert(0, tempLetter);
                        } else if (token.equals("a") && tokenBuffer.length() == 1 && i >= inputLength) {
                            // special for names ending with 'a' since that reads as 'aa'
                            // This might also be a bad idea for a transliteration platform since other languages
                            // don't necessarily follow this rule.
                            buffer.insert(0, vowelMap.get("A"));
                        }

                    } else {
                        buffer.insert(0, tempLetter);
                    }
                    followsConsonant = Arrays.asList(englishTokenGroups.get("consonants")).contains(token);
                    // trim the buffer
                    tokenBuffer = tokenBuffer.substring(tokenLength);
                    break;
                } else if (j == maxTokenLength - 1) {
                    if (followsConsonant) {
                        followsConsonant = false;
                    }
                    buffer.insert(0, token);
                    tokenBuffer = tokenBuffer.substring(1);
                }
            }
        }

        return buffer.reverse().toString();
    }

    // Get the maximum token length from the list of all possible tokens in the from language
    private int computeMaxTokenLength(CharacterMapping mappingData) {
        int max = -1;
        for (Map.Entry<String, String[]> entry : mappingData.getMapping().entrySet()) {
            for (String token : entry.getValue()) {
                if (token.length() > max){
                    max = token.length();
                }
            }
        }

        return max;
    }

    // Create a 1-to-1 token mapping between from and to languages
    private HashMap<String, String> createTokenMap(CharacterMapping from, CharacterMapping to) {
        HashMap<String, String> tokenMapping = new HashMap<>();

        HashMap<String, String[]> fromMapping = from.getMapping();
        HashMap<String, String[]> toMapping = to.getMapping();
        HashMap<String, String[]> alternates = from.getAlternateMapping();
        for (Map.Entry<String, String[]> fromEntry : fromMapping.entrySet()) {
            String[] fromTokens = fromEntry.getValue();
            String[] toTokens = toMapping.get(fromEntry.getKey());

            // create mapping for for all letters and alternates
            for (int i = 0; i < fromTokens.length; i++) {
                String key = fromTokens[i];
                String value = toTokens[i];
                tokenMapping.put(key, value);
                String[] alt = alternates.get(key);
                if (alt != null) {
                    for (String currentAlternate : alt) {
                        tokenMapping.put(currentAlternate, value);
                    }
                }
            }
        }
        return tokenMapping;
    }

    // Create alternate mapping for vowel marks when vowels follow consonants
    private HashMap<String, String> createVowelMap(CharacterMapping from, CharacterMapping to) {
        HashMap<String, String> vowelMap = new HashMap<>();

        String[] fromList = from.getMapping().get("vowels");
        String[] toList = to.getMapping().get("vowelMarks");
        HashMap<String, String[]> alternates = from.getAlternateMapping();

        for (int i = 1; i < fromList.length; i++) {
            String key = fromList[i];
            String value = toList[i - 1];
            vowelMap.put(key, value);
            String[] alt = alternates.get(key);
            if (alt != null) {
                for (String currentAlternate : alt) {
                    vowelMap.put(currentAlternate, value);
                }
            }
        }

        return vowelMap;
    }
}
