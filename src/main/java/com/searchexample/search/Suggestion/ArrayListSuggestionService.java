package com.searchexample.search.Suggestion;

import com.searchexample.search.Suggestion.InvertIndexNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArrayListSuggestionService {

    private Map<Character,InvertIndexNode> invertIndexNodeHashMap = new HashMap<>();

    public Map<Character, InvertIndexNode> getInvertIndexNodeHashMap() {
        return invertIndexNodeHashMap;
    }

    public void updateInverIndexMap(String str, int id){
        for(short i = 0;i<str.length();i++){
            char curLetter = str.charAt(i);
            //如果该字母曾经出现过
            if(invertIndexNodeHashMap.containsKey(curLetter)){
                InvertIndexNode curNode = invertIndexNodeHashMap.get(curLetter);
                //更新两个List
                int index = curNode.hasWord(id);
                curNode.update(id,i,index);
            }else{
                //未出现过该字母，new一个node，添加到map当中
                InvertIndexNode curNode = new InvertIndexNode();
                curNode.update(id,i,-1);
                invertIndexNodeHashMap.put(curLetter,curNode);
            }
        }
    }

    public List<Integer> getSuggestion(String str){
        Map<Integer,Short> curMap = new HashMap<>();
        List<Integer> suggestion = new ArrayList<>();
        dfs(suggestion,str,curMap,0);
        return suggestion;
    }

    private void dfs(List<Integer> suggestion,String str,Map<Integer,Short> remMap,int i){
        if(i == str.length()) {
            for (Integer pattern : remMap.keySet()) {
                suggestion.add(pattern);
            }
        }else{
            char[] curLetter = new char[2];
            if(Character.isUpperCase(str.charAt(i))){
                curLetter[0] = Character.toLowerCase(str.charAt(i));
                curLetter[1] = str.charAt(i);
            }else{
                curLetter[0] = str.charAt(i);
                curLetter[1] = Character.toUpperCase(str.charAt(i));
            }
            Map<Integer, Short> patternMap = new HashMap<>();
            for(int j = 0;j<2;j++){
                if(invertIndexNodeHashMap.containsKey(curLetter[j])) {
                    InvertIndexNode curNode = invertIndexNodeHashMap.get(curLetter[j]);
                    //patternMap = new HashMap<>();
                    if (!remMap.isEmpty()) {
                        for (Integer pattern : remMap.keySet()) {
                            //当前字母倒排表与remMap存在交集，更新nextPosition
                            int index = curNode.hasWord(pattern);
                            if (index != -1) {
                                short next = curNode.nextPosition(remMap.get(pattern), index);
                                if (next != -1) {
                                    patternMap.put(pattern, next);
                                }
                            }
                        }

                    } else if (remMap.isEmpty() && i == 0) {
                        List<Integer> curWordIdList = curNode.getWordIdList();
                        List<List<Short>> curLetterPositions = curNode.getLetterPositions();
                        for (int n = 0; n < curWordIdList.size(); n++) {
                            patternMap.put(curWordIdList.get(n), curLetterPositions.get(n).get(0));
                        }
                    }
                }

            }
            dfs(suggestion, str, patternMap, ++i);
        }
    }
}
