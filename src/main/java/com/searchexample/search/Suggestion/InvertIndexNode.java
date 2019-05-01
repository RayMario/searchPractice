package com.searchexample.search.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class InvertIndexNode {
    private List<Integer> wordIdList = new ArrayList<>();
    private List<List<Short>> letterPositions = new ArrayList<>();

    public List<List<Short>> getLetterPositions() {
        return letterPositions;
    }

    public List<Integer> getWordIdList() {
        return wordIdList;
    }

    //更新两个list，
    //其中wordId代表数据库当中的单词Id索引
    //position代表当前字母在当前单词当中的位置
    // 其中index是hasword二分查找的结果，代表指定wordId在wordIdList当中下标
    public void update(int wordId,short position,int index){

        //已经存在这个元素，只需要更新letterPositions
        if(index != -1){
            List<Short> posList = letterPositions.get(index);
            posList.add(position);
            letterPositions.set(index,posList);
        }else{
            //还不存在这个元素,直接将当前wordId添加进去，新建一个List包含当前position，添加在末尾
            wordIdList.add(wordId);
            List<Short> posList = new ArrayList<>();
            posList.add(position);
            letterPositions.add(posList);
        }
    }

    //二分法查找wordIdList当中是否已经存在给定的元素,
    //如果存在返回对应元素的下标，如果不存在返回-1
    public int hasWord(int id){
        int min = 0;
        int max = wordIdList.size()-1;
        int mid;
        while(min<=max){
            mid = min + ((max-min) >>> 1);
            if(wordIdList.get(mid)>id){
                max = mid-1;
            }else if(wordIdList.get(mid)<id){
                min = mid+1;
            }else{
                return mid;
            }
        }
        return -1;
    }

    //根据上一次position，查询是否有满足条件的下一个position。
    // 其中index代表某个单词Id在wordIdList当中的下标，是对一个InvertIndexNode实例hasWord（wordId）的结果
    //如果存在满足条件的位置，则返回该位置，否则返回-1.
    //比如在help单词当中，上一次对e字母应当记录位置1，此次搜索l字母，此函数应当返回2；如果搜索h字母，返回-1
    public short nextPosition(short remPosition, int index){
        List<Short> posList = letterPositions.get(index);
        for(int i = 0;i<posList.size();i++){
            if(posList.get(i)>remPosition){
                return (short)posList.get(i);
            }
        }
        return -1;
    }

}
