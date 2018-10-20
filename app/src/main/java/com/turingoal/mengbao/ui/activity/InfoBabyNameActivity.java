package com.turingoal.mengbao.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改宝宝昵称
 */
@Route(path = ConstantActivityPath.INFO_BABY_NAME)
public class InfoBabyNameActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定按钮
    @BindView(R.id.etName)
    EditText etName; // 昵称

    @Override
    protected int getLayoutID() {
        return R.layout.activity_info_baby_name;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_info_baby_name); // title
        tvEnd.setVisibility(View.VISIBLE); // 显示确定按钮
        etName.setText(TgSystemHelper.getBabyNickname()); // 宝宝昵称
        if (TgStringUtil.isEmpty(etName.getText().toString().trim())) { // 如果没有内容不可点击
            tvEnd.setEnabled(false);
        } else {
            tvEnd.setEnabled(true);
        }
        etName.addTextChangedListener(nameTextWatcher);
        etName.setSelection(etName.getText().toString().trim().length()); // 光标移动到文本框末尾
    }

    /**
     * 昵称EditText监听
     */
    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() > 0) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * 修改宝宝昵称
     */
    private void nickNameRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_CHILD_NICKNAME, getHttpTaskKey());
        request.params("id", TgSystemHelper.getBabyId());
        request.params("nickName", etName.getText().toString().trim());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    TgApplication.getTgUserPreferences().setBabyNickname(etName.getText().toString().trim());
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.tvEnd: // 确定按钮
                nickNameRequest();
                break;
            default:
                break;
        }
    }
}
