package com.turingoal.mengbao.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改个性签名
 */
@Route(path = ConstantActivityPath.INFO_PARENTS_RESUME)
public class InfoParentsResumeActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定
    @BindView(R.id.etResume)
    ClearEditText etResume; // 个性签名
    @BindView(R.id.tvCount)
    TextView tvCount; // 字数

    @Override
    protected int getLayoutID() {
        return R.layout.activity_info_parents_resume;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_info_parents_resume);
        tvEnd.setVisibility(View.VISIBLE); // 显示确定按钮
        etResume.setText(TgStringUtil.isEmpty(TgSystemHelper.getResume()) ? getString(R.string.default_resume) : TgSystemHelper.getResume()); // 个性签名
        tvCount.setText(etResume.getText().toString().trim().length() + "/140");
        if (TgStringUtil.isEmpty(etResume.getText().toString().trim())) { // 如果没有内容不可点击
            tvEnd.setEnabled(false);
        } else {
            tvEnd.setEnabled(true);
        }
        etResume.addTextChangedListener(resumeTextWatcher);
        etResume.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // EditText清空事件
            @Override
            public void textClear() {
                tvEnd.setEnabled(false);
            }
        });
        etResume.setSelection(etResume.getText().toString().trim().length()); // 光标移动到文本框末尾
    }

    /**
     * 个性签名EditText监听
     */
    private TextWatcher resumeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            tvCount.setText(editable.length() + "/140");
            if (editable.toString().trim().length() > 0 && editable.toString().trim().length() <= 140) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * 修改资料
     */
    private void resumeRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_USER_RESUME, getHttpTaskKey());
        request.params("id", TgSystemHelper.getUserId());
        request.params("signature", etResume.getText().toString().trim());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    TgApplication.getTgUserPreferences().setResume(etResume.getText().toString().trim());
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
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.tvEnd:
                resumeRequest();
                break;
            default:
                break;
        }
    }
}
