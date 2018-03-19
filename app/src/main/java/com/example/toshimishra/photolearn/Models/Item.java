package com.example.toshimishra.photolearn.Models;



public class Item {
    String itemID;
    String titleID;
    String photoURL;
    public Item(){}
    public  Item(String titleID,String photoURL,String itemID){
        this.titleID = titleID;
        this.photoURL = photoURL;
        this.itemID = itemID;

        //TODO update ID
    }
    public void update(String photoURL){
        //todo delete old photo from cloud and upload new
        this.photoURL = photoURL;
    }
    public void delete(){

        //Todo delte photo from cloud
    }

    public String getItemID() {
        return itemID;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
