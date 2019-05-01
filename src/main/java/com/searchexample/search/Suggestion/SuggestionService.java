package com.searchexample.search.Suggestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionService {
    @Autowired
    private GetSuggestionDao getSuggestionDao;
    @Autowired
    ArrayListSuggestionService arrayListSuggestionService;
    @Autowired
    HashMapSuggestionService hashMapSuggestionService;

    public void updateArrayListSuggestion(String str){
        try{
            //如果库中已经存在这个单词，直接获取id
            getSuggestionDao.selectByWord(str);
        }catch (Exception e){
            //如果没有——npe，则需要将str入库
            getSuggestionDao.addWord(str);
        }finally {
            int id = getSuggestionDao.selectByWord(str);
            arrayListSuggestionService.updateInverIndexMap(str,id);
        }
    }
    public void updateHashMapSuggestion(String str) {
        try {
            //如果库中已经存在这个单词，直接获取id
            getSuggestionDao.selectByWord(str);
        } catch (Exception e) {
            //如果没有——npe，则需要将str入库
            getSuggestionDao.addWord(str);
        } finally {
            int id = getSuggestionDao.selectByWord(str);
            hashMapSuggestionService.addInversIndex(str, id);
        }
    }

    //通过Idlist从数据库当中拿到String List
    public List<String> getSuggestion(List<Integer> idList){
        List<String> wordSuggestions = new ArrayList<>();
        for(int i = 0;i<idList.size();i++){
            wordSuggestions.add(getSuggestionDao.selectById(idList.get(i)));
        }
        return wordSuggestions;
    }
}
