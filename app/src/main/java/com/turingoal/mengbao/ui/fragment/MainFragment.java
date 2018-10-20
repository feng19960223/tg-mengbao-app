package com.turingoal.mengbao.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.sunfusheng.marqueeview.MarqueeView;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseFragment;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.bean.FunItemBean;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.common.biz.domain.Dynamic;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;
import com.turingoal.mengbao.ui.adapter.MainFunAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页
 */

public class MainFragment extends TgBaseFragment {
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar; // 头像
    @BindView(R.id.tvBabyName)
    TextView tvBabyName; // 名字
    @BindView(R.id.tvBabyVersion)
    TextView tvBabyVersion; // 等级
    @BindView(R.id.tvSchool)
    TextView tvSchool; // 学校
    @BindView(R.id.tvUpgrade)
    TextView tvUpgrade; // 增值
    @BindView(R.id.tvFlower)
    TextView tvFlower; // 奖励值
    @BindView(R.id.llDynamic)
    LinearLayout llDynamic; // 动态视图
    @BindView(R.id.smvDynamic)
    MarqueeView smvDynamic; // 动态条
    @BindView(R.id.rvFun)
    RecyclerView rvFun; // 主菜单
    private MainFunAdapter mAdapter = new MainFunAdapter(); // adapter
    private static final int SPAN_COUNT = 3; // 功能列数
    private List<FunItemBean> funItemBeanList = new ArrayList<>(); // 功能菜单数据list
    private final int index = 2; // 显示通知数的item
    private boolean isDynamic = true; // 加载动态

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initialized() {
        // 性别
        Drawable genderDrawable = getResources().getDrawable(R.drawable.ic_women); // 性别图片
        if (getString(R.string.man).equals(TgSystemHelper.getBabyGender())) {
            genderDrawable = getResources().getDrawable(R.drawable.ic_men); // 性别图片
        }
        genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight()); // 必须加上这一句，否则不显示图片
        tvBabyName.setCompoundDrawables(genderDrawable, null, null, null);
        // 学校
        tvSchool.setText(TgSystemHelper.getSchoolName());
        initRecyclerAndAdapter();
        initData();
        VersionUtil.setVersion(tvBabyVersion, TgApplication.upgrade, TgSystemHelper.getBabyGender());
        tvUpgrade.setText("x " + TgApplication.upgrade); // 成长值
        tvFlower.setText("x " + TgApplication.flower); // 奖励值
    }

    /**
     * 初始化菜单数据
     */
    private void initData() {
        funItemBeanList.add(new FunItemBean(R.drawable.ic_courses, getString(R.string.fun_courses), ConstantActivityPath.COURSES)); // 课表
        funItemBeanList.add(new FunItemBean(R.drawable.ic_cookbook, getString(R.string.fun_cookbook), ConstantActivityPath.COOKBOOK)); // 食谱
        funItemBeanList.add(new FunItemBean(R.drawable.ic_address_book, getString(R.string.fun_address_book), ConstantActivityPath.ADDRESS_BOOK)); // 通讯录
        funItemBeanList.add(new FunItemBean(R.drawable.ic_attendance, getString(R.string.fun_attendance), ConstantActivityPath.ATTENDANCE)); // 考勤
        funItemBeanList.add(new FunItemBean(R.drawable.ic_class_pictures, getString(R.string.fun_class_pictures), ConstantActivityPath.ALBUM)); // 相册
        funItemBeanList.add(new FunItemBean(R.drawable.ic_homework, getString(R.string.fun_homework), ConstantActivityPath.HOMEWORK)); // 亲子作业
        funItemBeanList.add(new FunItemBean(R.drawable.ic_comments, getString(R.string.fun_comments), ConstantActivityPath.COMMENTS)); // 点评
        funItemBeanList.add(new FunItemBean(R.drawable.ic_school_photo, getString(R.string.fun_school_photo), ConstantActivityPath.SCHOOL_PHOTO)); // 校园风采
        funItemBeanList.add(new FunItemBean(R.drawable.ic_love_remark, getString(R.string.fun_love_remark), ConstantActivityPath.LOVE_REMARK)); // 爱心备注
        funItemBeanList.add(new FunItemBean(R.drawable.ic_message, getString(R.string.fun_message), ConstantActivityPath.MESSAGE)); // 消息中心
        FunItemBean funItemBean = new FunItemBean(R.drawable.ic_inform_item, getString(R.string.fun_inform), ConstantActivityPath.INFORM);
        funItemBean.setCount(TgSystemHelper.getInformCount()); // 未读数量
        funItemBeanList.add(index, funItemBean); // 通知,将通知加到index位置
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获得基本信息
        initUserInfo();
        initInforCount();
        initDynamic();
    }

    @Override
    public void onHiddenChanged(final boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isDynamic) {
            if (hidden) { // fragment不可见的时候，动态不运行，可见的时候，重新运行，否则有重影出现
                smvDynamic.startFlipping();
            } else {
                smvDynamic.stopFlipping();
            }
        }
    }

    /**
     * 孩子基本信息
     */
    private void initUserInfo() {
        GlideUtil.loadImage(getContext(), TgSystemHelper.getBabyAvatar(), ivAvatar);
        if (TgStringUtil.isEmpty(TgSystemHelper.getBabyName())) {
            tvBabyName.setText(R.string.app_name);
        } else {
            tvBabyName.setText(TgSystemHelper.getBabyName());
            if (!TgStringUtil.isEmpty(TgSystemHelper.getBabyNickname())) {
                tvBabyName.append("（ " + TgSystemHelper.getBabyNickname() + " ）");
            }
        }
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_CHILD_DETAIL, getHttpTaskKey());
        request.params("id", TgSystemHelper.getBabyId());
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    Log.i("aaaaaaaaaaaaa", "successHandler: "+result.getData().toString());
                    Child child = TgJsonUtil.jsonResultBean2Object(result, Child.class);
                    if (child != null) {
                        // 头像
                        TgApplication.getTgUserPreferences().setBabyAvatar(child.getAvatar());
                        GlideUtil.loadImage(getContext(), TgSystemHelper.getBabyAvatar(), ivAvatar);
                        // 名字
                        TgApplication.getTgUserPreferences().setBabyName(child.getRealname());
                        TgApplication.getTgUserPreferences().setBabyNickname(child.getNickname());
                        if (TgStringUtil.isEmpty(TgSystemHelper.getBabyName())) {
                            tvBabyName.setText(R.string.app_name);
                        } else {
                            tvBabyName.setText(TgSystemHelper.getBabyName());
                            if (!TgStringUtil.isEmpty(TgSystemHelper.getBabyNickname())) {
                                tvBabyName.append("（ " + TgSystemHelper.getBabyNickname() + " ）");
                            }
                        }
                        TgApplication.upgrade = child.getValueGrowth();
                        tvUpgrade.setText("x " + TgApplication.upgrade); // 成长值
                        TgApplication.flower = child.getValueFlower();
                        tvFlower.setText("x " + TgApplication.flower); // 花朵值
                        VersionUtil.setVersion(tvBabyVersion, TgApplication.upgrade, TgSystemHelper.getBabyGender());
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 未读消息数量
     */
    private void initInforCount() {
        funItemBeanList.get(index).setCount(TgSystemHelper.getInformCount()); // 将第二个数据的消息数改为一个随机的数字
        mAdapter.notifyItemChanged(index); // adapter刷新
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_INFORM_COUNT, getHttpTaskKey());
        request.params("userId", TgSystemHelper.getUserId());
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    Map<String, Object> map = (Map<String, Object>) result.getData();
                    int informCount = (Integer) map.get("count");
                    TgApplication.getTgUserPreferences().setInformCount(informCount);
                    funItemBeanList.get(index).setCount(TgSystemHelper.getInformCount()); // 将第二个数据的消息数改为一个随机的数字
                    mAdapter.notifyItemChanged(index); // adapter刷新
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 动态
     */
    private void initDynamic() {
        if (isDynamic) {
            // 获得动态
            PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_DYNAMIC, getHttpTaskKey());
            request.params("schoolId", TgSystemHelper.getSchoolId());
            request.execute(new TgHttpCallback(null) {
                @Override
                public void successHandler(final TgResponseBean result) {
                    if (result.isSuccess()) {
                        if (result.getData() == null) {
                            return;
                        }
                        List<Dynamic> dynamics = TgJsonUtil.jsonResultBean2List(result, Dynamic.class);
                        if (dynamics != null) {
                            List<String> list = new ArrayList<>();
                            for (Dynamic dynamic : dynamics) {
                                list.add(String.format(getString(R.string.dynamic), dynamic.getChildRealname(), dynamic.getCreateTimeStr4DateTime(), dynamic.getValueFlower()));
                            }
                            smvDynamic.startWithList(list); // 动态
                        }
                    } else {
                        isDynamic = false;
                        llDynamic.setVisibility(View.GONE);
                        TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                    }
                }

                @Override
                public void onError(Response<TgResponseBean> response) {
                    super.onError(response);
                    isDynamic = false;
                    llDynamic.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvFun.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mAdapter.openLoadAnimation(); // 动画
        rvFun.setAdapter(mAdapter);
        mAdapter.setNewData(funItemBeanList);
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivAvatar, R.id.tvBabyVersion, R.id.tvUpgrade, R.id.tvFlower, R.id.ivDynamic})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivAvatar: // 头像
                TgSystemHelper.handleIntent(ConstantActivityPath.INFO_BABY);
                break;
            case R.id.tvBabyVersion: // 成长规则页面
                TgSystemHelper.handleIntent(ConstantActivityPath.GROW_RULE);
                break;
            case R.id.tvUpgrade: // 成长明细页面
            case R.id.tvFlower:
                TgSystemHelper.handleIntent(ConstantActivityPath.GROW_DETAIL);
                break;
            case R.id.ivDynamic:
                llDynamic.setVisibility(View.GONE);
                smvDynamic.stopFlipping();
                isDynamic = false;
                break;
            default:
                break;
        }
    }
}

/**
 * 等级帮助类
 */
class VersionUtil {
    private static final int version1 = 10;
    private static final int version2 = 20;
    private static final int version3 = 30;
    private static final int version4 = 40;
    private static final int version5 = 50;
    private static final int version6 = 60;
    private static final int version7 = 70;
    private static final int version8 = 80;
    private static final int version9 = 90;
    private static final int version10 = 100;
    private static final int version11 = 110;
    private static final int version12 = 120;
    private static final int version13 = 130;
    private static final int version14 = 140;
    private static final int version15 = 150;
    private static final int version16 = 160;
    private static final int version17 = 170;
    private static final int version18 = 180;
    private static final int version19 = 190;
    private static final int version20 = 200;
    private static final int version21 = 210;
    private static final int version22 = 220;
    private static final int version23 = 230;
    private static final int version24 = 240;

    /**
     * 暴露方法，设置图片和文字
     */
    public static void setVersion(final TextView textView, final int count, final String gender) {
        textView.setText(getVersionString(textView.getContext(), count, gender));
        textView.setCompoundDrawables(getVersionDrawable(textView.getContext(), count, gender), null, null, null);
    }

    /**
     * 设置等级图片
     */
    private static Drawable getVersionDrawable(final Context context, final int count, final String gender) {
        Drawable versionDrawable;
        if (count < version1) {
            versionDrawable = context.getDrawable(R.drawable.ic_version11);
        } else if (count < version2) {
            versionDrawable = context.getDrawable(R.drawable.ic_version12);
        } else if (count < version3) {
            versionDrawable = context.getDrawable(R.drawable.ic_version13);
        } else if (count < version4) {
            versionDrawable = context.getDrawable(R.drawable.ic_version14);
        } else if (count < version5) {
            versionDrawable = context.getDrawable(R.drawable.ic_version15);
        } else if (count < version6) {
            versionDrawable = context.getDrawable(R.drawable.ic_version21);
        } else if (count < version7) {
            versionDrawable = context.getDrawable(R.drawable.ic_version22);
        } else if (count < version8) {
            versionDrawable = context.getDrawable(R.drawable.ic_version23);
        } else if (count < version9) {
            versionDrawable = context.getDrawable(R.drawable.ic_version24);
        } else if (count < version10) {
            versionDrawable = context.getDrawable(R.drawable.ic_version25);
        } else if (count < version11) {
            versionDrawable = context.getDrawable(R.drawable.ic_version31);
        } else if (count < version12) {
            versionDrawable = context.getDrawable(R.drawable.ic_version32);
        } else if (count < version13) {
            versionDrawable = context.getDrawable(R.drawable.ic_version33);
        } else if (count < version14) {
            versionDrawable = context.getDrawable(R.drawable.ic_version34);
        } else if (count < version15) {
            versionDrawable = context.getDrawable(R.drawable.ic_version35);
        } else if (count < version16) {
            versionDrawable = context.getDrawable(R.drawable.ic_version41);
        } else if (count < version17) {
            versionDrawable = context.getDrawable(R.drawable.ic_version42);
        } else if (count < version18) {
            versionDrawable = context.getDrawable(R.drawable.ic_version43);
        } else if (count < version19) {
            versionDrawable = context.getDrawable(R.drawable.ic_version44);
        } else if (count < version20) {
            versionDrawable = context.getDrawable(R.drawable.ic_version45);
        } else if (count < version21) {
            versionDrawable = context.getDrawable(R.drawable.ic_version54);
        } else if (count < version22) {
            versionDrawable = context.getDrawable(R.drawable.ic_version52);
        } else if (count < version23) {
            versionDrawable = context.getDrawable(R.drawable.ic_version53);
        } else if (count < version24) {
            versionDrawable = context.getDrawable(R.drawable.ic_version54);
        } else {
            versionDrawable = context.getDrawable(R.drawable.ic_version55);
        }
        versionDrawable.setBounds(0, 0, versionDrawable.getMinimumWidth(), versionDrawable.getMinimumHeight()); // 必须加上这一句，否则不显示图片
        return versionDrawable;
    }

    /**
     * 设置等级文字
     */
    private static String getVersionString(final Context context, final int count, final String gender) {
        String versionStr;
        if (count < version1) {
            versionStr = context.getString(R.string.version_1);
        } else if (count < version2) {
            versionStr = context.getString(R.string.version_2);
        } else if (count < version3) {
            versionStr = context.getString(R.string.version_3);
        } else if (count < version4) {
            versionStr = context.getString(R.string.version_4);
        } else if (count < version5) {
            versionStr = context.getString(R.string.version_5);
        } else if (count < version6) {
            versionStr = context.getString(R.string.version_6);
        } else if (count < version7) {
            versionStr = context.getString(R.string.version_7);
        } else if (count < version8) {
            versionStr = context.getString(R.string.version_8);
        } else if (count < version9) {
            versionStr = context.getString(R.string.version_9);
        } else if (count < version10) {
            versionStr = context.getString(R.string.version_10);
        } else if (count < version11) {
            versionStr = context.getString(R.string.version_11);
        } else if (count < version12) {
            versionStr = context.getString(R.string.version_12);
        } else if (count < version13) {
            versionStr = context.getString(R.string.version_13);
        } else if (count < version14) {
            versionStr = context.getString(R.string.version_14);
        } else if (count < version15) {
            versionStr = context.getString(R.string.version_15);
        } else if (count < version16) {
            versionStr = context.getString(R.string.version_16);
        } else if (count < version17) {
            versionStr = context.getString(R.string.version_17);
        } else if (count < version18) {
            versionStr = context.getString(R.string.version_18);
        } else if (count < version19) {
            versionStr = context.getString(R.string.version_19);
        } else if (count < version20) {
            versionStr = context.getString(R.string.version_20);
        } else if (count < version21) {
            versionStr = context.getString(R.string.version_21);
        } else if (count < version22) {
            versionStr = context.getString(R.string.version_22);
        } else if (count < version23) {
            versionStr = context.getString(R.string.version_23);
        } else if (count < version24) {
            versionStr = context.getString(R.string.version_24);
        } else {
            versionStr = context.getString(R.string.version_25);
        }
        return versionStr;
    }
}
