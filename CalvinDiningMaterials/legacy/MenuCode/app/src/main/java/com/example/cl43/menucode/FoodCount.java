package com.example.cl43.menucode;

/**
 * Created by cl43 on 11/2/2016.
 */
public class FoodCount {

    public int myCount;

    public FoodCount(int value){this.myCount = value;}

    void changeCount(int newCount){this.myCount = newCount;}

    int getCount(){
        return this.myCount;
    }
}
