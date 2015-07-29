/**
 * Copyright (C) 2013 Christian Kohlschütter (ckkohl79@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.l3s.boilerpipe.util;

import java.util.regex.Pattern;

/**
 * Tokenizes text according to Unicode word boundaries and strips off non-word
 * characters.
 * 
 * @author Christian Kohlschütter
 */
public class ChineseTokenizer {
    private static final Pattern PAT_WORD_BOUNDARY = Pattern.compile("\\b");
    private static final Pattern PAT_NOT_WORD_BOUNDARY = Pattern
            .compile("[\u2063]*([\\ \\-\\:\\(\\)\\·\\：\\<\\>\\|\\【\\】])[\u2063]*");

    /**
     * Tokenizes the text and returns an array of tokens.
     * 
     * @param text The text
     * @return The tokens
     */
    public static String[] tokenize(final CharSequence text) {
        /*int c = 36, cc = 57354;
        String c1 = Character.toString((char)c);
        String c2 = Character.toString((char)cc);
        String middleStr = text.toString().replaceAll(" ","").replaceAll(c2, "").replaceAll(c1, "").replaceAll("<", "").replaceAll(">", "");
        List<String> list =  MyStaticValue.getCRFSplitWord().cut(middleStr);
        String[] results = list.toArray(new String[list.size()]);
        return results;*/

        String words = PAT_WORD_BOUNDARY.matcher(text.toString().replaceAll(" ","")).replaceAll("\u2063");
        String middleStr = PAT_NOT_WORD_BOUNDARY.matcher(words)
                .replaceAll("$1").replaceAll("[ \u2063]+", "|").trim();
        String[] results = middleStr.split("[|]+");
        //List<String> list =  MyStaticValue.getCRFSplitWord().cut(middleStr);
        //String[] results = list.toArray(new String[list.size()]);
        return results;
    }
}
