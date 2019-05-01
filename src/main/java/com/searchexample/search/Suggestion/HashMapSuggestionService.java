package com.searchexample.search.Suggestion;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HashMapSuggestionService {

    private Map<Character,Map<Integer,List<Short>>> trieMap = new HashMap<>();

    public void addInversIndex(String str,int id){
        for(int i = 0;i<str.length();i++){
            char curLetter = str.charAt(i);
            //如果该字母曾经出现过
            if(trieMap.containsKey(curLetter)){
                Map curMap = trieMap.get(curLetter);
                //如果该字母已经在该单词当中出现过
                if(curMap.containsKey(id)){
                    List<Short> positions = (List<Short>) curMap.get(id);
                    positions.add((short)i);
                    curMap.put(id,positions);
                }else{
                    //没出现过直接添加
                    List<Short> positions = new ArrayList<>();
                    positions.add((short)i);
                    curMap.put(id,positions);
                }

                //该字母未曾出现过
            }else{
                Map<Integer,List<Short>> curMap = new HashMap<>();
                List<Short> positions = new ArrayList<>();
                positions.add((short)i);
                curMap.put(id,positions);
                trieMap.put(curLetter,curMap);
            }
        }
    }

    //使用两级HashMap，由字母定位到单词Id的时间复杂度为O（1），
    //对某个字母遍历其倒排索引需要O（M），其中M为某个字母对应的所有单词个数。
    // 查找某个字母在当前单词Id下的位置需要遍历List——遍历时间最坏可能性为List的长度L
    // 但是由于单词长度有限，同时一个有效单词内不会大量重复出现同一个字母，因此List的长度也是较短的。
    //因此总的查询时间应当是O（N*M*L），其中N为用户输入的单词的长度，M为字母对应的倒排索引单词个数，L为字母在某个单词中的位置列表长度。
    //比如对hello world与help两个单词当中的所有字母构建了倒排索引，现在输入hewl，则本次查询次数为输入长度4*首字母h对应的单词个数2*l的位置列表长度3.
    public List<Integer> getSuggestion(String str){
        if(str != ""){
            str = Util.removeSpace(str);
        }
        Map<Integer,Short> curMap = new HashMap<>();
        List<Integer> suggestion = new ArrayList<>();
        dfs(suggestion,str,curMap,0);
        return suggestion;
    }

    private void dfs(List<Integer> suggestion,String str,Map<Integer,Short> remMap,int i){

        if(i == str.length()){
            for(Integer pattern:remMap.keySet()){
                suggestion.add(pattern);
            }
        }else {
            char[] curLetter = new char[2];
            if(Character.isUpperCase(str.charAt(i))){
                curLetter[0] = Character.toLowerCase(str.charAt(i));
                curLetter[1] = str.charAt(i);
            }else{
                curLetter[0] = str.charAt(i);
                curLetter[1] = Character.toUpperCase(str.charAt(i));
            }
            Map<Integer, Short> patternMap = new HashMap<>();
            for(int j = 0;j<2;j++) {
                if (trieMap.containsKey(curLetter[j])) {
                    //从字母-单词倒排表当中取得当前字母的倒排表
                    Map<Integer, List<Short>> curMap = trieMap.get(curLetter[j]);
                    //交集表，key记录单词的交集，value记录对应单词中当前字母的最小位置

                    //
                    if (!remMap.isEmpty()) {
                        for (Integer pattern : remMap.keySet()) {

                            if (remMap.containsKey(pattern) && curMap.containsKey(pattern)) {
                                short last = lastPosition(remMap.get(pattern),curMap.get(pattern));
                                if(last != -1) {
                                    patternMap.put(pattern, last);
                                }
                            }
                        }
                        //第一个字母初始化交集表,记录出现
                    } else if (remMap.isEmpty() && i == 0) {
                        for(Integer pattern:curMap.keySet()){
                            patternMap.put(pattern,(short)curMap.get(pattern).get(0));
                        }
                    }

                }
            }
            dfs(suggestion, str, patternMap, ++i);
        }
    }
    //在当前字母的位置列表当中，找到刚好大于remPosition的值。
    private short lastPosition(Short remPosition,List<Short> positions){
        for(short i=0; i<positions.size(); i++){
            if(positions.get(i)>remPosition){
                return (short) positions.get(i);
            }
        }
        return -1;
    }

    public Map<Character, Map<Integer, List<Short>>> getTrieMap() {
        return trieMap;
    }
}
