package com.turingoal.mengbao.ui.activity;

import android.view.View;
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
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.Comments;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 老师点评详情
 */
@Route(path = ConstantActivityPath.COMMENTS_DETAIL)
public class CommentsDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivEnd)
    ImageView ivEnd; // 回复
    @BindView(R.id.tvSource)
    TextView tvSource; // 老师点评发布人
    @BindView(R.id.tvDate)
    TextView tvDate; // 老师点评时间
    @BindView(R.id.tvContent)
    TextView tvContent; // 老师点评内容
    @BindView(R.id.ivFlower)
    ImageView ivFlower;
    @BindView(R.id.tvReply)
    TextView tvReply; // 家长的回复內容
    @Autowired
    Comments comments; // 从list传递过来的item对象

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comments_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_comments_detail);
        ivEnd.setImageResource(R.drawable.ic_add_comment);
        ivEnd.setVisibility(TgStringUtil.isEmpty(comments.getParentReply()) ? View.VISIBLE : View.GONE); // 如果已经回复过了，就不在显示回复按钮
        tvSource.setText(comments.getUserRealnameTeacher());
        tvDate.setText(comments.getCreateTimeStr4DateTime());
        tvContent.setText(comments.getContent());
        ivFlower.setVisibility(TgBtsConstantYesNo.YES.equals(comments.getFlower()) ? View.VISIBLE : View.GONE);
        tvReply.setVisibility(TgStringUtil.isEmpty(comments.getParentReply()) ? View.GONE : View.VISIBLE); // 家长是否回复
        tvReply.setText(comments.getParentReply());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TgStringUtil.isEmpty(comments.getParentReply())) { // 未回复
            PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COMMENTS_DETAIL, getHttpTaskKey());
            request.params("id", comments.getId());
            request.execute(new TgHttpCallback(this) {
                @Override
                public void successHandler(final TgResponseBean result) {
                    if (result.isSuccess()) {
                        if (result.getData() == null) {
                            return;
                        }
                        Comments comments = TgJsonUtil.jsonResultBean2Object(result, Comments.class);
                        if (comments != null) {
                            ivEnd.setVisibility(TgStringUtil.isEmpty(comments.getParentReply()) ? View.VISIBLE : View.GONE); // 如果已经回复过了，就不在显示回复按钮
                            tvReply.setVisibility(TgStringUtil.isEmpty(comments.getParentReply()) ? View.GONE : View.VISIBLE); // 家长是否回复
                            tvReply.setText(comments.getParentReply());
                        }
                    } else {
                        TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                    }
                }
            });
        }
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.ivEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.ivEnd:
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.COMMENTS_REPLY, "comments", comments);
                break;
            default:
                break;
        }
    }
}
