package com.turingoal.mengbao.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 亲子作业详情
 */
@Route(path = ConstantActivityPath.HOMEWORK_DETAIL)
public class HomeworkDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvHomeworkTitle)
    TextView tvHomeworkTitle; // 亲子作业title
    @BindView(R.id.tvSource)
    TextView tvSource; // 亲子作业发布人
    @BindView(R.id.tvDate)
    TextView tvDate; // 亲子作业时间
    @BindView(R.id.tvContent)
    TextView tvContent; // 亲子作业内容
    @BindView(R.id.ivFlower)
    ImageView ivFlower; // 是否已经送过花了
    @BindView(R.id.tvScore)
    TextView tvScore; // 分数
    @BindView(R.id.tvAppraise)
    TextView tvAppraise; // 评价内容
    @BindView(R.id.btnSubmit)
    Button btnSubmit; // 打分按钮
    @Autowired
    HomeworkRecord homeworkRecord; // 从list传递过来的item对象

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework_detail);
        tvHomeworkTitle.setText(homeworkRecord.getHomeworkTitle());
        tvSource.setText(homeworkRecord.getUserRealname());
        tvDate.setText(homeworkRecord.getCreateTimeStr4DateTime());
        tvContent.setText(homeworkRecord.getHomeworkContent());
        ivFlower.setVisibility(TgBtsConstantYesNo.YES.equals(homeworkRecord.getFlower()) ? View.VISIBLE : View.GONE); // 如果已经送过花，显示小花图标
        if (TgBtsConstantYesNo.YES.equals(homeworkRecord.getSubmited())) { // 已完成
            tvScore.setText(getScore(homeworkRecord.getScore()));
            tvAppraise.setText(homeworkRecord.getContent());
            btnSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TgBtsConstantYesNo.NO.equals(homeworkRecord.getSubmited())) { // 未完成
            PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK_DETAIL, getHttpTaskKey());
            request.params("id", homeworkRecord.getId());
            request.execute(new TgHttpCallback(this) {
                @Override
                public void successHandler(final TgResponseBean result) {
                    if (result.isSuccess()) {
                        if (result.getData() == null) {
                            return;
                        }
                        HomeworkRecord homeworkRecord = TgJsonUtil.jsonResultBean2Object(result, HomeworkRecord.class);
                        if (homeworkRecord != null) {
                            if (TgBtsConstantYesNo.YES.equals(homeworkRecord.getSubmited())) { // 已完成
                                tvScore.setText(getScore(homeworkRecord.getScore()));
                                tvAppraise.setText(homeworkRecord.getContent());
                                btnSubmit.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                    }
                }
            });
        }
    }

    /**
     * 分数转化
     */
    private String getScore(final int score) {
        String scoreStr;
        switch (score) {
            case 1:
                scoreStr = "0.5";
                break;
            case 2:
                scoreStr = "1.0";
                break;
            case 3:
                scoreStr = "1.5";
                break;
            case 4:
                scoreStr = "2.0";
                break;
            case 5:
                scoreStr = "2.5";
                break;
            case 6:
                scoreStr = "3.0";
                break;
            case 7:
                scoreStr = "3.5";
                break;
            case 8:
                scoreStr = "4.0";
                break;
            case 9:
                scoreStr = "4.5";
                break;
            case 10:
                scoreStr = "5.0";
                break;
            default:
                scoreStr = "0.0";
                break;
        }
        return scoreStr;
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnSubmit})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.btnSubmit: // 提交
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.HOMEWORK_APPRAISE, "homeworkRecord", homeworkRecord); // 亲子作业打分
                break;
            default:
                break;
        }
    }
}
