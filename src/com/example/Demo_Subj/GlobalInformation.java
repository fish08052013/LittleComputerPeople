package com.example.Demo_Subj;

import java.util.List;

/**
 * Created by thasti-note on 04.12.2014.
 * A static class to determine system information from everywhere
 */
public class GlobalInformation {
    private static int screenWidth;
    private static int screenHeight;
    private static int currentRoom;
    private static List<Room> roomList;
    private static Subject subject;
    private static int tick = 10;               //10 Millisekunden
    private static int realTimeDay = 4;         //Dauer eines internen Tages in realen Minuten
    private static int realTimeNight = 1;       //Dauer einer internen Nacht in realen Minuten

    private GlobalInformation(){};

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        GlobalInformation.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        GlobalInformation.screenWidth = screenWidth;
    }

    public static int getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(int currentRoom) {
        GlobalInformation.currentRoom = currentRoom;
    }

    public static List<Room> getRoomList() {
        return roomList;
    }

    public static void setRoomList(List<Room> roomList) {
        GlobalInformation.roomList = roomList;
    }

    public static Subject getSubject() {
        return subject;
    }

    public static void setSubject(Subject subject) {
        GlobalInformation.subject = subject;
    }

    public static int getTick(){
        return tick;
    }

    public static int getRealTimeDay(){
        return realTimeDay;
    }

    public static int getRealTimeNight(){
        return realTimeNight;
    }
}
