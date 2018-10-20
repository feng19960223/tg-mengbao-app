package com.turingoal.mengbao.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.constants.TgConstantLoveRemarksType;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 增加爱心备注
 */
@Route(path = ConstantActivityPath.LOVE_ADD)
public class LoveAddActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定
    @BindView(R.id.rgCause)
    RadioGroup rgCause; // 标题
    @BindView(R.id.etRemakes)
    ClearEditText etRemakes; // 备注
    @BindView(R.id.tvCount)
    TextView tvCount; // 字数

    @Override
    protected int getLayoutID() {
        return R.layout.activity_love_add;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_love_add);
        tvEnd.setVisibility(View.VISIBLE);
        tvEnd.setEnabled(false);
        etRemakes.addTextChangedListener(remakesTextWatcher);
    }

    /**
     * 备注EditText监听
     */
    private TextWatcher remakesTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            tvCount.setText(editable.toString().trim().length() + "/140");
            if (editable.toString().trim().length() > 0) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.tvEnd: // 增加
                addRequest();
                break;
            default:
                break;
        }
    }

    /**
     * 增加爱心备注
     */
    private void addRequest() {
        // 得到选中的类型
        String typeStr = ((RadioButton) findViewById(rgCause.getCheckedRadioButtonId())).getText().toString();
        int type = TgConstantLoveRemarksType.LEAVE;
        if (typeStr.equals(getString(R.string.attendance2))) {
            type = TgConstantLoveRemarksType.MEDICINE;
        } else if (typeStr.equals(getString(R.string.attendance3))) {
            type = TgConstantLoveRemarksType.DIET;
        } else if (typeStr.equals(getString(R.string.attendance4))) {
            type = TgConstantLoveRemarksType.OTHERS;
        }
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_LOVE_REMARK_ADD, getHttpTaskKey());
        request.params("content", etRemakes.getText().toString().trim());
        request.params("type", type);
        request.params("userId", TgSystemHelper.getUserId());
        request.params("userCodeNum", TgSystemHelper.getUserCodeNum());
        request.params("userRealname", TgSystemHelper.getUsername());
        request.params("childId", TgSystemHelper.getBabyId());
        request.params("childCodeNum", TgSystemHelper.getBabyCodeNum());
        request.params("childRealname", TgSystemHelper.getBabyName());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.params("schoolCodeNum", TgSystemHelper.getSchoolCodeNum());
        request.params("schoolTitle", TgSystemHelper.getSchoolName());
        request.params("classId", TgSystemHelper.getClassId());
        request.params("classCodeNum", TgSystemHelper.getClassCodeNum());
        request.params("classTitle", TgSystemHelper.getclassName());
        request.params("classGrade", TgSystemHelper.getClassGrade());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }
}
