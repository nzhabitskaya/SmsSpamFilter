package com.mobile.android.smsspamfilter.database;


import java.util.Comparator;

public class Result1Database {//implements Comparable<Result1Database> {
long id;
int isChecked;
int type;
public String value;

public Result1Database() {
}

public Result1Database(long id, int isChecked, int type, String value) {
    this.id = id;
    this.isChecked = isChecked;
    this.type = type;
    this.value = value;
}

public long getId() {
    return id;
}
/*
public void setId(long id) {
    this.id = id;
}
*/
public int getIsChecked() {
    return isChecked;
}
/*
public void setIsChecked(int isChecked) {
    this.isChecked = isChecked;
}
*/
public int getType() {
    return type;
}
/*
public void setType(int type) {
    this.type = type;
}
*/
public String getValue() {
    return value;
}
/*
public void setValue(String value) {
    this.value = value;
}
 */
public static class SortByName implements Comparator<Result1Database>
{
    @Override
    public int compare(Result1Database t, Result1Database t1) 
    {
        return (int) t.value.compareTo(t1.value);      
    }

}

}

