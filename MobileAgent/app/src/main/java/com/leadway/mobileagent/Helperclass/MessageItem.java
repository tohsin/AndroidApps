package com.leadway.mobileagent.Helperclass;

import java.util.ArrayList;

public class MessageItem {
    private String name;
    private String description;
    private String date;

    public MessageItem(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    private static int lastContactId = 0;

    public static ArrayList<MessageItem> createmessageList(int numContacts,String which_colon) {
        ArrayList<MessageItem> contacts = new ArrayList<MessageItem>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new MessageItem(which_colon+"Person " + ++lastContactId,
                    "I am exatly numer dash msg"+ ++lastContactId,"15th may"));
        }

        return contacts;
    }

}
