package com.searchexample.search.Suggestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SuggestionController {
    @Autowired
    SuggestionService suggestionService;
    @Autowired
    ArrayListSuggestionService arrayListSuggestionService;
    @Autowired
    HashMapSuggestionService hashMapSuggestionService;

    @RequestMapping(path = {"/test"},method = {RequestMethod.GET})
    @ResponseBody
    public String testSuggestion(){

        suggestionService.updateArrayListSuggestion("hello world");
        suggestionService.updateArrayListSuggestion("help");
        suggestionService.updateArrayListSuggestion("hello enum");

        //使用ArrayList方式的倒排索引执行补全查询
        //构建测试用例：
        String[] testExamles = new String[]{"hel e","he","","wo","hes"," he "};
        StringBuilder stringBuilder = new StringBuilder();
        for(String testExample:testExamles) {
            List<Integer> suggestionIdList1 = arrayListSuggestionService.getSuggestion(testExample);
            List<String> suggestionWordList1 = suggestionService.getSuggestion(suggestionIdList1);
            stringBuilder.append("测试用例——");
            stringBuilder.append(testExample+"，");
            stringBuilder.append("输出结果—— ");
            stringBuilder.append(suggestionWordList1+" ;");
            stringBuilder.append(" ");

        }
        return stringBuilder.toString();
    }
}
