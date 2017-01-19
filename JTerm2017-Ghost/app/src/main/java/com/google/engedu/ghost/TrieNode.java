/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TrieNode {
    // A map from the next character in the alphabet to the trie node containing those words
    private HashMap<Character, TrieNode> children;
    // If true, this node represents a complete word.
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    /**
     * Add the string as a child of this trie node.
     *
     * @param s String representing partial suffix of a word.
     */
    public void add(String s) {
        // TODO(you): add String s to this node.

        HashMap<Character, TrieNode> current = children;

//        for (int i = 0; i < s.length(); i++) {
//            if (!current.containsKey(s.charAt(i))) {
//                current.put(s.charAt(i), new TrieNode());
//            }
//
//            if(i != s.length() -1){
//                current = current.get(s.charAt(i)).children;
//            }else {
//                break;
//            }
//        }
//        current.get(s.charAt(s.length()-1)).isWord = true;

        if(s.equals("")){
            isWord = true;
        } else{
            Character toAdd = s.charAt(0);
            String rest = s.substring(1);

            if(!children.containsKey(toAdd)){
                children.put(toAdd, new TrieNode());
            }

            children.get(toAdd).add(rest);
        }
    }

    /**
     * Determine whether this node is part of a complete word for the string.
     *
     * @param s String representing partial suffix of a word.
     * @return
     */
    public boolean isWord(String s) {
        // TODO(you): determine whether this node is part of a complete word for String s.

//        TrieNode current =  this;
//
//        for(int i = 0; i < s.length(); i++){
//            if (!current.children.containsKey(s.charAt(i))){
//                return false;
//            }
//            current = current.children.get(s.charAt(i));
//        }
//
//        return current.isWord;

        if(s.equals("")){
            return isWord;
        }
        if(!children.containsKey(s.charAt(0))){
            return false;
        }
        return children.get(s.charAt(0)).isWord(s.substring(1));
    }

    /**
     * Find **any complete word** with this partial segment.
     *
     * @param s String representing partial suffix of a word.
     * @return
     */
    public String getAnyWordStartingWith(String s) {
        // TODO(you):
        TrieNode current = this;
        for(int i = 0; i < s.length(); i++){
            if(!current.children.containsKey(s.charAt(i))){
                return null;
            }
            current = current.children.get(s.charAt(i));
        }
        // while not a word
        while(!current.isWord){
            Character c = current.children.keySet().iterator().next();
            s += c;
            current = current.children.get(c);
        }
        return s;
    }

    /**
     * Find a good complete word with this partial segment.
     *
     * Definition of "good" left to implementor.
     *
     * @param s String representing partial suffix of a word.
     * @return
     */
    public String getGoodWordStartingWith(String s) {
        TrieNode current = this;

        for(int i = 0; i < s.length(); i++){
            if(!current.children.containsKey(s.charAt(i))){
                return null;
            }
            current = current.children.get(s.charAt(i));
        }

        // ArrayList<String> selection = new ArrayList<>();

        String goodWord = "";
        String word= s;

        for(Character c : current.children.keySet()){

            TrieNode temp = current;
            word = s;
            int wordCount = s.toCharArray().length;

            while(!temp.isWord){
                word += c;
                wordCount++;
                temp = temp.children.get(c);
            }

            // if have even number of letters after prefix
            if(wordCount % 2 == 0) {
                goodWord = word;
            }
        }

        return goodWord;
    }

    public String DavidgetAnyWordStartingWith(String s){
        Character head;
        String rest;

        if(s.isEmpty()){
            if(isWord){
                return "";
            } else{
                Set<Character> keySet = children.keySet();
                if(keySet.isEmpty()){
                    return null;
                }
                head = keySet.iterator().next();
                rest = "";
            }
        } else{
            head = s.charAt(0);
            rest = s.substring(1);
            if(!children.containsKey(head)){
                return null;
            }
        }
        String word = children.get(head).getAnyWordStartingWith(rest);

        if(word == null){
            return null;
        }

        return head + word;
    }
}