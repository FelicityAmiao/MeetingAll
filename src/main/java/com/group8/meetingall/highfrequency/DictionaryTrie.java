package com.group8.meetingall.highfrequency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DictionaryTrie implements Comparable<DictionaryTrie>  {
    private static final int ARRAY_LENGTH_LIMIT = 3;
    private Character nodeChar;
    private DictionaryTrie[] childrenTrieArray;
    private Map<Character, DictionaryTrie> childrenTrieMap;
    private static final Map<Character, Character> charMap = new HashMap<>(16, 0.95f);
    private int storeSize = 0;
    private boolean isWord = false;

    public DictionaryTrie(Character nodeChar) {
        if (nodeChar == null) {
            throw new IllegalArgumentException("Argument can not be empty");
        }
        this.nodeChar = nodeChar;
    }

    public Hit match(char[] charArray, int begin, int length) {
        return match(charArray,begin,length,null);
    }

    private Hit match(char[] charArray, int begin, int length, Hit searchHit) {

        if (searchHit == null) {
            searchHit = new Hit();
            searchHit.setBegin(begin);
        }
        else {
            searchHit.setUnmatch();
        }
        searchHit.setEnd(begin);

        Character keyChar = new Character(charArray[begin]);
        DictionaryTrie dictionaryTrie = null;

        DictionaryTrie[] trieArray = this.childrenTrieArray;
        Map<Character, DictionaryTrie> childrenTrieMap = this.childrenTrieMap;

        if (trieArray != null) {
            DictionaryTrie keySegment = new DictionaryTrie(keyChar);
            int position = Arrays.binarySearch(trieArray, 0, this.storeSize, keySegment);
            if (position >= 0) {
                dictionaryTrie = trieArray[position];
            }
        }
        else if (childrenTrieMap != null) {
            dictionaryTrie = childrenTrieMap.get(keyChar);
        }

        if (dictionaryTrie != null) {
            if (length > 1) {
                return dictionaryTrie.match(charArray, begin + 1, length - 1, searchHit);
            }
            else if (length == 1) {
                if (dictionaryTrie.isWord) {
                    searchHit.setMatch();
                }
                if (dictionaryTrie.hasNextNode()) {
                    searchHit.setPrefix();
                    searchHit.setMatchedDictionaryTrie(dictionaryTrie);
                }
                return searchHit;
            }

        }
        return searchHit;
    }

    private boolean hasNextNode() {
        return this.storeSize > 0;
    }

    private DictionaryTrie[] getChildrenArray() {
        if (this.childrenTrieArray == null) {
            synchronized (this) {
                if (this.childrenTrieArray == null) {
                    this.childrenTrieArray = new DictionaryTrie[ARRAY_LENGTH_LIMIT];
                }
            }
        }
        return this.childrenTrieArray;
    }

    public void fillTrie(char[] charArray) {
        this.fillTrie(charArray, 0, charArray.length, true);
    }

    private synchronized void fillTrie(char[] charArray, int begin, int length, boolean create) {
        Character beginChar = new Character(charArray[begin]);
        Character keyChar = charMap.get(beginChar);
        if (keyChar == null) {
            charMap.put(beginChar, beginChar);
            keyChar = beginChar;
        }
        DictionaryTrie dictionaryTrie = lookforSegment(keyChar, create);
        if (dictionaryTrie != null) {
            if (length > 1) {
                dictionaryTrie.fillTrie(charArray, begin + 1, length - 1, create);
            }
            else if (length == 1) {
                dictionaryTrie.isWord = create;
            }
        }
    }

    private DictionaryTrie lookforSegment(Character keyChar, boolean create) {
        DictionaryTrie dictionaryTrie = null;
        if (this.storeSize <= ARRAY_LENGTH_LIMIT) {
            DictionaryTrie[] segmentArray = getChildrenArray();
            DictionaryTrie keySegment = new DictionaryTrie(keyChar);
            int position = Arrays.binarySearch(segmentArray, 0, this.storeSize, keySegment);
            if (position >= 0) {
                dictionaryTrie = segmentArray[position];
            }
            if (dictionaryTrie == null && create) {
                dictionaryTrie = keySegment;
                if (this.storeSize < ARRAY_LENGTH_LIMIT) {
                    segmentArray[this.storeSize] = dictionaryTrie;
                    this.storeSize++;
                    Arrays.sort(segmentArray, 0, this.storeSize);
                }
                else {
                    Map<Character, DictionaryTrie> segmentMap = getChildrenMap();
                    migrate(segmentArray, segmentMap);
                    segmentMap.put(keyChar, dictionaryTrie);
                    this.storeSize++;
                    this.childrenTrieArray = null;
                }
            }
        }
        else {
            Map<Character, DictionaryTrie> segmentMap = getChildrenMap();
            dictionaryTrie = segmentMap.get(keyChar);
            if (dictionaryTrie == null && create) {
                dictionaryTrie = new DictionaryTrie(keyChar);
                segmentMap.put(keyChar, dictionaryTrie);
                this.storeSize++;
            }
        }
        return dictionaryTrie;
    }

    private Map<Character, DictionaryTrie> getChildrenMap() {
        if (this.childrenTrieMap == null) {
            synchronized (this) {
                if (this.childrenTrieMap == null) {
                    this.childrenTrieMap = new HashMap<Character, DictionaryTrie>(ARRAY_LENGTH_LIMIT * 2, 0.8f);
                }
            }
        }
        return this.childrenTrieMap;
    }

    private void migrate(DictionaryTrie[] segmentArray, Map<Character, DictionaryTrie> segmentMap) {
        for (DictionaryTrie segment : segmentArray) {
            if (segment != null) {
                segmentMap.put(segment.nodeChar, segment);
            }
        }
    }

    @Override
    public int compareTo(DictionaryTrie o) {
        return this.nodeChar.compareTo(o.nodeChar);
    }
}
