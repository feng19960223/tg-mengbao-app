package com.turingoal.mengbao.constants;

/**
 * 常量-Activity路径
 */
public interface ConstantActivityPath {
    String PACKAGE = "/path/mengbao/"; // 所有Activity路径，前面都要加上这个字段，防止起名字出错，页面路由必须是2个以上字符串组成的
    String MAIN = PACKAGE + "main/index"; // Main主界面
    String ABOUT = PACKAGE + "about"; // 关于
    String LOGIN = PACKAGE + "login"; // 登录
    String FORGET = PACKAGE + "forget"; // 忘记密码
    String VERIFICATION = PACKAGE + "verification"; // 验证码
    String NEW_PASSWORD = PACKAGE + "new/password"; // 设置新密码
    String AGREEMENT = PACKAGE + "agreement"; // 用户协议
    String COOKBOOK = PACKAGE + "cookbook"; // 食谱
    String COLLECT = PACKAGE + "collect"; // 收藏
    String SETTING = PACKAGE + "setting"; // 基础设置
    String ADDRESS_BOOK = PACKAGE + "address/book"; // 通讯录
    String COURSES = PACKAGE + "courses"; // 课表
    String GROW_DETAIL = PACKAGE + "grow/detail"; // 成长明细
    String GROW_RULE = PACKAGE + "grow/rule"; // 成长规则
    String HOMEWORK = PACKAGE + "homework"; // 亲子作业
    String HOMEWORK_DETAIL = PACKAGE + "homework/detail"; // 亲子作业详情
    String HOMEWORK_APPRAISE = PACKAGE + "homework/appraise"; // 亲子作业打分
    String ALBUM = PACKAGE + "album"; // 相册
    String PICTURES = PACKAGE + "pictures"; // 班级相册
    String PICTURES_DETAIL = PACKAGE + "pictures/detail"; // 班级相册详情
    String ATTENDANCE = PACKAGE + "attendance"; // 考勤记录
    String COMMENTS = PACKAGE + "comments"; // 老师点评
    String COMMENTS_DETAIL = PACKAGE + "comments/detail"; // 老师点评详情
    String COMMENTS_REPLY = PACKAGE + "comments/reply"; // 回复老师点评
    String INFORM = PACKAGE + "inform"; // 通知
    String INFORM_DETAIL = PACKAGE + "inform/detail"; // 通知详情
    String INFO_BABY = PACKAGE + "info/baby"; // 萌宝资料
    String INFO_BABY_NAME = PACKAGE + "info/baby/name"; // 修改萌宝名字
    String INFO_PARENTS = PACKAGE + "info/parents"; // 家长资料
    String INFO_PARENTS_RESUME = PACKAGE + "info/parents/resume"; // 修改家长签名
    String CHANGE_PHONE = PACKAGE + "change/phone"; // 修改手机号
    String CHANGE_VERIFICATION = PACKAGE + "change/verification"; // 修改手机号后到验证码
    String CHANGE_PASSWORD = PACKAGE + "change/password"; // 修改密码
    String SCHOOL_PHOTO = PACKAGE + "school/photo"; // 学校风采相册
    String SCHOOL_PHOTO_DETAIL = PACKAGE + "school/photo/detail"; // 学校风采照片详情
    String LOVE_REMARK = PACKAGE + "love/remark"; // 爱心备注
    String LOVE_ADD = PACKAGE + "love/add"; // 爱心备注
    String BINDING_ACCOUNT = PACKAGE + "binding/account"; // 账号绑定
    String BINDING_USER = PACKAGE + "binding/account"; // 绑定用户
    String MESSAGE = PACKAGE + "message"; // 消息
}
