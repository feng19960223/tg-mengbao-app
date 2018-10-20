package com.turingoal.mengbao.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.GrowthDetail;

/**
 * 成长明细adapter
 */

public class GrowDetailAdapter extends BaseQuickAdapter<GrowthDetail, TgBaseViewHolder> {
    public GrowDetailAdapter() {
        super(R.layout.item_grow_detail);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final GrowthDetail growthDetail) {
        helper.setText(R.id.tvSource, growthDetail.getOrigin())
                .setText(R.id.tvDate, growthDetail.getCreateTimeStr4DateTime())
                .setGone(R.id.tvUpgrade, growthDetail.getValueGrowth() != 0)
                .setGone(R.id.tvFlower, growthDetail.getValueFlower() != 0)
                .setText(R.id.tvUpgrade, "" + growthDetail.getValueGrowth())
                .setText(R.id.tvFlower, "" + growthDetail.getValueGrowth());
    }
}
