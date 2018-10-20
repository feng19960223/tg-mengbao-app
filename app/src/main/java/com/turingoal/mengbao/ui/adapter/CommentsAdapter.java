package com.turingoal.mengbao.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.Comments;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.constants.ConstantActivityPath;

/**
 * 老师点评adapter
 */
public class CommentsAdapter extends BaseQuickAdapter<Comments, TgBaseViewHolder> {
    public CommentsAdapter() {
        super(R.layout.item_comments);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Comments comments) {
        helper.setText(R.id.tvContent, comments.getContent())
                .setText(R.id.tvReply, comments.getParentReply())
                .setText(R.id.tvDate, comments.getCreateTimeStr4DateTime())
                .setGone(R.id.ivFlower, TgBtsConstantYesNo.YES.equals(comments.getFlower()))
                .setText(R.id.tvSource, comments.getUserRealnameTeacher());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.COMMENTS_DETAIL, "comments", comments); // 亲子作业详情页面
            }
        });
    }
}
