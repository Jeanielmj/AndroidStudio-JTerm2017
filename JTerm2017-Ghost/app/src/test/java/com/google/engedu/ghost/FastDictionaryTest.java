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

package com.google.engedu.ghost;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FastDictionaryTest {

    FastDictionary dictionary;

    @Before
    public void setup(){
        List<String> list = new ArrayList<>();
        list.add("accept");
        list.add("brave");
        list.add("cat");
        list.add("catch");
        list.add("boom");
        list.add("bone");
        dictionary = new FastDictionary(list);
    }

    @Test
    public void testIsWord() {
        // TODO(you): Add some tests!
        Assert.assertTrue(dictionary.isWord("boom"));
        Assert.assertFalse(dictionary.isWord("bo"));
        Assert.assertTrue(dictionary.isWord("bone"));
        Assert.assertTrue(dictionary.isWord("cat"));
        Assert.assertFalse(dictionary.isWord("catc"));
        Assert.assertTrue(dictionary.isWord("catch"));
        Assert.assertFalse(dictionary.isWord("matchh") );
        Assert.assertTrue(dictionary.isWord("cat") );
    }

    @Test
    public void testGetAnyWordStartingWith() {
        // TODO(you): Add some tests!
        assertEquals(dictionary.getAnyWordStartingWith("don"), null);
        assertEquals(dictionary.getAnyWordStartingWith("acc"), "accept");
        assertEquals(dictionary.getAnyWordStartingWith("boo"), "boom");
        assertEquals(dictionary.getAnyWordStartingWith("cat"), "cat");
        assertEquals(dictionary.getAnyWordStartingWith("don"), null);
        assertEquals(dictionary.getAnyWordStartingWith("brat"), null);
    }

    @Test
    public void testDavidGetAnyWordStartingWith() {
        // TODO(you): Add some tests!
        assertEquals(dictionary.DavidgetAnyWordStartingWith("don"), null);
        assertEquals(dictionary.DavidgetAnyWordStartingWith("acc"), "accept");
        assertEquals(dictionary.DavidgetAnyWordStartingWith("boo"), "boom");
        assertEquals(dictionary.DavidgetAnyWordStartingWith("cat"), "cat");
        assertEquals(dictionary.DavidgetAnyWordStartingWith("don"), null);
        assertEquals(dictionary.DavidgetAnyWordStartingWith("brat"), null);
    }

    public void testgetGoodWordStartingWith() {
        // TODO(you): Add some tests!
       // assertEquals(dictionary.getGoodWordStartingWith("don"), null);
       // assertEquals(dictionary.getGoodWordStartingWith("acc"), "accept");

    }
}

