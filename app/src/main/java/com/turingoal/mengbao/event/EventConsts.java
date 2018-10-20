package com.turingoal.mengbao.event;

/**
 * 打点取值
 * 1
 * EventLogger.logEvent(this,EventConsts.e_Id_A); // 点击
 * 2
 * EventLogger.logEvent(this, EventConsts.e_Id_A, EventConsts.e_Key_A, "value"); // 传入一个值
 * 3
 * Map<String, String> map = new HashMap<>(); // 传入多个值
 * map.put(EventConsts.e_Key_A, "value");
 * EventLogger.logEvent(this,EventConsts. e_Id_A, map);
 */
public interface EventConsts {
    String e_Id_Fun_Path = "funPath"; // 主页面功能点击
    String e_Id_Intent = "intent"; // 页面跳转
    String e_Id_AvatarParent = "avatarParent"; // 家长头像
    String e_Id_AvatarChild = "avatarChild"; // 孩子头像

    String e_Key_Fun_Path = "funPath"; // 主页面功能
    String e_Key_Intent = "intent"; // 页面跳转

    String e_Id_A = ""; // 点击位置id值
    String e_Key_A = ""; // 点击参数key值
}
