package com.doodle.smallSlackBot;

import java.util.HashMap;

public class User {
    private String ID;
    private double userCurrAvg;
    private int userMsgAmount;
    public static HashMap<String, User> usersAvgs = new HashMap<>();
    public static HashMap<String, String> usersNamesToIDs = new HashMap();
    private static double totalAvg = 0;
    private static int totalAmountOfMsgs = 0;
    private static int amountOfMsgInLastPrint = 0;


    public User (String userID) {
        setID(userID);
        setUserCurrAvg(0);
        setUserMsgAmount(0);
    }

    public User addNewNumberToAvg (double number) {
        setUserCurrAvg(calcNewAvg (getUserCurrAvg(), number, getUserMsgAmount()));
        setUserMsgAmount(getUserMsgAmount() + 1);
        setTotalAvg(calcNewAvg(getTotalAvg(), number,getTotalAmountOfMsgs()));
        setTotalAmountOfMsgs(getTotalAmountOfMsgs() + 1);
        return this;
    }

    private static double calcNewAvg (double firstAvg, double secAvg, int firstMsgAmount) {
        return ((firstAvg*firstMsgAmount + secAvg) / (firstMsgAmount+1));
    }

    public static double getAvgPerUser (String slackUserName) {
        User user = usersAvgs.get(usersNamesToIDs.get(slackUserName));
        if (null == user) {
            return 0;
        }
        return user.getUserCurrAvg();
    }

    public static void cleanStaticVars() {
        usersAvgs = new HashMap<>();
        setAmountOfMsgInLastPrint(0);
        setTotalAmountOfMsgs(0);
        setTotalAvg(0);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getUserCurrAvg() {
        return userCurrAvg;
    }

    public void setUserCurrAvg(double userCurrAvg) {
        this.userCurrAvg = userCurrAvg;
    }

    public int getUserMsgAmount() {
        return userMsgAmount;
    }

    public void setUserMsgAmount(int userMsgAmount) {
        this.userMsgAmount = userMsgAmount;
    }

    public static double getTotalAvg() {
        return totalAvg;
    }

    public static void setTotalAvg(double totalAvg) {
        User.totalAvg = totalAvg;
    }

    public static int getTotalAmountOfMsgs() {
        return totalAmountOfMsgs;
    }

    public static void setTotalAmountOfMsgs(int totalAmountOfMsgs) {
        User.totalAmountOfMsgs = totalAmountOfMsgs;
    }

    public static int getAmountOfMsgInLastPrint() {
        return amountOfMsgInLastPrint;
    }

    public static void setAmountOfMsgInLastPrint(int amountOfMsgInLastPrint) {
        User.amountOfMsgInLastPrint = amountOfMsgInLastPrint;
    }
}
