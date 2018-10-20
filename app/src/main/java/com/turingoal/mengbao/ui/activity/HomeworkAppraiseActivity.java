package com.turingoal.mengbao.ui.activity;

import android.support.v7.widget.AppCompatRatingBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 亲子作业打分
 */
@Route(path = ConstantActivityPath.HOMEWORK_APPRAISE)
public class HomeworkAppraiseActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvScore)
    TextView tvScore; // 作业评价结果
    @BindView(R.id.rbHomework)
    AppCompatRatingBar rbHomework; // 作业评价
    @BindView(R.id.etAppraise)
    EditText etAppraise; // 作业评价
    @BindView(R.id.tvCount)
    TextView tvCount; // 字数
    @BindView(R.id.btnAppraise)
    Button btnAppraise; // 确定打分
    @Autowired
    HomeworkRecord homeworkRecord; // 从list传递过来的item对象

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework_appraise;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework_appraise);
        etAppraise.addTextChangedListener(appraiseTextWatcher);
        rbHomework.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                if (fromUser) { // fromUser rating数量是否发生改变
                    ratingBar.setRating(rating);
                }
                tvScore.setText(String.valueOf(rating));
            }
        });
    }

    /**
     * 作业评价EditText监听
     */
    private TextWatcher appraiseTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            tvCount.setText("" + editable.toString().trim().length() + "/140");
            if (editable.length() > 0 && editable.length() <= 140) {
                btnAppraise.setEnabled(true);
            } else {
                btnAppraise.setEnabled(false);
            }
        }
    };

    /**
     * 打分评价网络请求
     */
    private void appraiseRequest() {
        int score = (int) (rbHomework.getRating() * 2);
        String appraise = etAppraise.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK_APPRAISE, getHttpTaskKey());
        request.params("id",homeworkRecord.getId());
        request.params("score", score);
        request.params("content", appraise);
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

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnAppraise})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.btnAppraise:
                appraiseRequest();
                break;
            default:
                break;
        }
    }
}
