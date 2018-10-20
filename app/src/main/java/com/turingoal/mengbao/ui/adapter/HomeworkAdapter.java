package com.turingoal.mengbao.ui.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.constants.ConstantActivityPath;

/**
 * 亲子作业adapter
 */
public class HomeworkAdapter extends BaseQuickAdapter<HomeworkRecord, TgBaseViewHolder> {
    public HomeworkAdapter() {
        super(R.layout.item_homework);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final HomeworkRecord homeworkRecord) {
        helper.setText(R.id.tvTitle, homeworkRecord.getHomeworkTitle())
                .setText(R.id.tvContent, homeworkRecord.getHomeworkContent())
                .setGone(R.id.ivFlower, TgBtsConstantYesNo.YES.equals(homeworkRecord.getFlower()))
                .setText(R.id.tvDate, homeworkRecord.getCreateTimeStr4DateTime())
                .setText(R.id.tvSource, homeworkRecord.getUserRealname())
                .setText(R.id.tvState, TgBtsConstantYesNo.YES.equals(homeworkRecord.getSubmited()) ? mContext.getString(R.string.homework_yes) : mContext.getString(R.string.homework_no))
                .setTextColor(R.id.tvState, TgBtsConstantYesNo.YES.equals(homeworkRecord.getSubmited()) ? Color.parseColor("#7CB342") : Color.parseColor("#F4511E"))
                .setGone(R.id.tvScore, TgBtsConstantYesNo.YES.equals(homeworkRecord.getSubmited()))
                .setText(R.id.tvScore, getScore(homeworkRecord.getScore()));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.HOMEWORK_DETAIL, "homeworkRecord", homeworkRecord); // 亲子作业详情页面
            }
        });
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
}
