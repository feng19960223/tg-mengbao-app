package com.turingoal.mengbao.ui.activity;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.constants.ConstantActivityPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成长规则
 */
@Route(path = ConstantActivityPath.GROW_RULE)
public class GrowRuleActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_grow_rule;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_grow_rule);
    }

    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
