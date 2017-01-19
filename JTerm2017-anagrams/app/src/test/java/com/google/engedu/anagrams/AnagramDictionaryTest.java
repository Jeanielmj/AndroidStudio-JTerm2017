/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.engedu.anagrams;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Tests for AnagramDictionary
 */

public class AnagramDictionaryTest{

    private AnagramDictionary anagrams;

    @Before
    public void buildDictionary() throws Exception {
        InputStream inputStream = new ByteArrayInputStream((
                "stop\npouts\ntypists\ntypo\ntypography\ntypos\n"
                ).getBytes());
        anagrams = new AnagramDictionary(inputStream);
    }

    @Test
    public void getAnagrams(){
        assertArrayEquals(
                anagrams.getAnagrams("typo").toArray(),
                new String[]{"typo"}
        );
    }

    @Test
    public void getAnagramsWithOneMoreLetter(){
        assertArrayEquals(
                anagrams.getAnagramsWithOneMoreLetter("typo").toArray(),
                new String[]{"typos"}
        );
    }

    @Test
    public void testSortLetters() {
        assertEquals(AnagramDictionary.sortLetters("a"), "a");
        assertEquals(AnagramDictionary.sortLetters("spot"), "opst");
        assertNotEquals(AnagramDictionary.sortLetters("spot"), "post");
    }

    @Test
    public void testIsAnagram() {
        assertTrue(AnagramDictionary.isAnagram("a", "a"));
        assertTrue(AnagramDictionary.isAnagram("stop", "pots"));
        assertFalse(AnagramDictionary.isAnagram("stop", "start"));
        //assertTrue(AnagramDictionary.isAnagram("stop", "start"));

    }

    @Test
    public void testIsGoodWord() {
       // TODO: This may need to be in AndroidTest
    }
}
