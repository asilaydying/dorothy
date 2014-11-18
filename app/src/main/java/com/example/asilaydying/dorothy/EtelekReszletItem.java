package com.example.asilaydying.dorothy;

/**
 * Created by supergep on 2014.11.14..
 */
public class EtelekReszletItem {
    private String addID;
    private String name;

    public EtelekReszletItem(String name, String addID) {
        this.addID = addID;
        this.name = name;
    }

    public String getAddID() {
        return addID;
    }

    public void setAddID(String addID) {
        this.addID = addID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
