package com.turingoal.mengbao.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.Album;
import com.turingoal.mengbao.constants.ConstantActivityPath;

/**
 * 相册adapter
 */
public class AlbumAdapter extends BaseQuickAdapter<Album, TgBaseViewHolder> {
    public AlbumAdapter() {
        super(R.layout.item_album);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Album album) {
        helper.setText(R.id.tvTitle, album.getTitle())
                .setText(R.id.tvDescription, album.getDescription())
                .setText(R.id.tvSize, "" + album.getPhotoSize());
        GlideUtil.load(mContext, album.getCoverImage(), (ImageView) helper.getView(R.id.ivCoverImage)); // 设置图片
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.PICTURES, "album", album); // 亲子作业详情页面
            }
        });
    }
}
