package com.turingoal.mengbao.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.bean.CollectBean;

/**
 * 收藏
 */
public class CollectAdapter extends BaseQuickAdapter<CollectBean, TgBaseViewHolder> {
    public CollectAdapter() {
        super(R.layout.item_collect);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final CollectBean collectBean) {
        helper.setText(R.id.tvTitle, collectBean.getTitle());
    }
}
