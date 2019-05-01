package com.searchexample.search.Suggestion;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GetSuggestionDao {

    @Select({"select word from suggestions where id = #{id}"})
    String selectById(int id);

    @Select({"select id from suggestions where word = #{word}"})
    int selectByWord(String word);

    @Insert({"insert into suggestions (word) values(#{word})"})
    int addWord(String word);
}
