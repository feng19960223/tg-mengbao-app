package com.turingoal.mengbao.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.android.photopicker.PhotoPicker;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;
import com.turingoal.mengbao.event.EventConsts;
import com.turingoal.mengbao.event.EventLogger;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 宝宝个人信息
 */
@Route(path = ConstantActivityPath.INFO_BABY)
public class InfoBabyActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivAvatarBg)
    ImageView ivAvatarBg; // 头像背景高斯模糊
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar; // 头像
    @BindView(R.id.tvBabyNickname)
    TextView tvBabyNickname; // 昵称
    @BindView(R.id.tvBabyName)
    TextView tvBabyName; // 名字
    @BindView(R.id.tvBabyGender)
    TextView tvBabyGender; // 性别
    @BindView(R.id.tvBabyBirthday)
    TextView tvBabyBirthday; // 生日
    @BindView(R.id.tvBabyRelation)
    TextView tvBabyRelation; // 关系
    @BindView(R.id.tvBabySchool)
    TextView tvBabySchool; // 学校
    @BindView(R.id.tvBabyClass)
    TextView tvBabyClass; // 班级
    @BindView(R.id.tvBabyTime)
    TextView tvBabyTime; // 入园时间

    @Override
    protected int getLayoutID() {
        return R.layout.activity_info_baby;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_info_baby);
        GlideUtil.loadImage(InfoBabyActivity.this, TgSystemHelper.getBabyAvatar(), ivAvatar); // 头像
        GlideUtil.loadBlur(InfoBabyActivity.this, TgSystemHelper.getBabyAvatar(), ivAvatarBg); // 头像背景
        tvBabyName.setText(TgSystemHelper.getBabyName()); // 姓名
        tvBabyBirthday.setText(TgSystemHelper.getBabyBirthday()); // 出生日期
        tvBabyGender.setText(TgSystemHelper.getBabyGender()); // 性别
        tvBabyRelation.setText(TgSystemHelper.getBabyRelation()); // 关系
        tvBabySchool.setText(TgSystemHelper.getSchoolName()); // 学校名称
        tvBabyClass.setText(TgSystemHelper.getclassName()); // 班级
        tvBabyTime.setText(TgSystemHelper.getClassDate()); // 入园时间
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvBabyNickname.setText(TgStringUtil.getSelectStr(TgSystemHelper.getBabyNickname(), getString(R.string.edit))); // 姓名
    }

    /**
     * 头像
     */
    private void avatar() {
        EventLogger.logEvent(EventConsts.e_Id_AvatarChild);
        PhotoPicker.selectPic(this, 600, new PhotoPicker.PicCallBack() {
            @Override
            public void onPicSelected(final String[] paths) {
                PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_CHILD_AVATAR, getHttpTaskKey());
                request.params("id", TgSystemHelper.getBabyId());
                request.params("avatar", new File(paths[0]));
                request.execute(new TgHttpCallback(InfoBabyActivity.this) {
                    @Override
                    public void successHandler(final TgResponseBean result) {
                        if (result.isSuccess()) {
                            TgApplication.getTgUserPreferences().setBabyAvatar(paths[0]);
                            GlideUtil.loadImage(InfoBabyActivity.this, paths[0], ivAvatar); // 头像
                            GlideUtil.loadBlur(InfoBabyActivity.this, paths[0], ivAvatarBg); // 头像背景
                        } else {
                            TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                        }
                    }
                });
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.flAvatar, R.id.llBabyNickname})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.flAvatar: // 头像
                avatar();
                break;
            case R.id.llBabyNickname: // 名字
                TgSystemHelper.handleIntent(ConstantActivityPath.INFO_BABY_NAME);
                break;
            default:
                break;
        }
    }
}
