package com.turingoal.mengbao.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.AlbumGroup;
import com.turingoal.mengbao.constants.ConstantActivityPath;

/**
 * 照片分组
 */
public class PicturesAdapter extends BaseQuickAdapter<AlbumGroup, TgBaseViewHolder> {

    public PicturesAdapter() {
        super(R.layout.item_pictures);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final AlbumGroup albumGroup) {
        helper.setText(R.id.tvSource, albumGroup.getDescription()) // 头
                .setText(R.id.tvDate, albumGroup.getCreateTimeStr4DateTime()); // 时间
        RecyclerView rvPicturesItem = helper.getView(R.id.rvPicturesItem);
        rvPicturesItem.setLayoutManager(new GridLayoutManager(mContext, 4));
        PicturesItemAdapter adapter = new PicturesItemAdapter(albumGroup.getAlbumPhotos());
        rvPicturesItem.setAdapter(adapter);
        // 点击查看
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PicturesItemAdapter.ImgUrlIndex = position;
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.PICTURES_DETAIL, "albumGroup", albumGroup);
            }
        });
    }
}
