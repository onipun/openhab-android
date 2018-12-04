package org.openhab.habdroid.beta;

public class Item {
    private static final String TAG = Item.class.getSimpleName();
    private String name;
    private  String state;
    private  String type;


    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
