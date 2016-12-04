package com.gudwns999.smartkeyapp;

public class ListViewItem {
    private int icon;
    private String name;
    private String day;

    public int getIcon(){return icon;}
    public String getName(){return name;}
    public String getDay(){return day;}

    public ListViewItem(int icon,String name,String day){
        this.icon = icon;
        this.name = name;
        this.day = day;
    }
}
