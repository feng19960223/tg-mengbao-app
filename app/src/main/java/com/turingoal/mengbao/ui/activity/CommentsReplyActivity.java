package com.turingoal.mengbao.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.Comments;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 回复点评
 */
@Route(path = ConstantActivityPath.COMMENTS_REPLY)
public class CommentsReplyActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etReply)
    EditText etReply; // 作业评价
    @BindView(R.id.tvCount)
    TextView tvCount; // 字数 140
    @BindView(R.id.btnReply)
    Button btnReply; // 回复按钮
    private final int COUNT_MAX_REPLY = 140; // 回复点评最大字数
    @Autowired
    Comments comments; // 从详情传递过来的对象

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comments_reply;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_comments_reply);
        etReply.addTextChangedListener(replyTextWatcher);
    }

    /**
     * 邀请评价EditText监听
     */
    private TextWatcher replyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            tvCount.setText("" + editable.toString().trim().length() + "/" + COUNT_MAX_REPLY);
            if (editable.toString().trim().length() > 0 && editable.toString().trim().length() <= COUNT_MAX_REPLY) {
                btnReply.setEnabled(true);
            } else {
                btnReply.setEnabled(false);
            }
        }
    };

    /**
     * 家长回复请求
     */
    private void replyRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COMMENTS_REPLY, getHttpTaskKey());
        request.params("id", comments.getId());
        request.params("parentReply", etReply.getText().toString().trim());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnReply})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.btnReply:
                replyRequest();
                break;
            default:
                break;
        }
    }
}
