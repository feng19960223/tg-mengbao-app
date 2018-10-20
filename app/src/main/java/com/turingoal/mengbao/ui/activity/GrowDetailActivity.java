package com.turingoal.mengbao.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.constants.TgConstantGetDataType;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.GrowthDetail;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;
import com.turingoal.mengbao.ui.adapter.GrowDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成长明细
 */
@Route(path = ConstantActivityPath.GROW_DETAIL)
public class GrowDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvGrowDetail)
    RecyclerView rvGrowDetail;
    private GrowDetailAdapter mAdapter = new GrowDetailAdapter();
    private int limitSize = 10; // 一次加载多少条数据
    private int pageSize = 2; // 第几页

    @Override
    protected int getLayoutID() {
        return R.layout.activity_grow_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_grow_detail);
        initRecyclerAndAdapter();
        getData(TgConstantGetDataType.INIT);
    }

    /**
     * 加载成长数据
     */
    private void getData(final int getDataType) {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_GROWTH_DETAIL, getHttpTaskKey());
        request.params("childId", TgSystemHelper.getBabyId());
        if (TgConstantGetDataType.LOAD_MORE == getDataType) {
            request.params("page", pageSize);
        }
        request.params("limit", limitSize);
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<GrowthDetail> growthDetails = TgJsonUtil.jsonResultBean2List(result, GrowthDetail.class);
                    if (growthDetails != null) {
                        if (TgConstantGetDataType.INIT == getDataType) {
                            pageSize = 2;
                            mAdapter.setNewData(growthDetails);
                        } else {
                            pageSize++;
                            // 去重
                            List<GrowthDetail> growthDetailList = new ArrayList<>();
                            growthDetailList.addAll(growthDetails);
                            growthDetailList.removeAll(mAdapter.getData());
                            mAdapter.addData(growthDetailList);
                        }
                        mAdapter.loadMoreComplete(); // 成功获取更多数据
                        if (growthDetails.size() < limitSize) {
                            mAdapter.loadMoreEnd(false); // 加载结束，没有数据，false显示没有更多数据，true不显示任何提示信息
                        }
                        if (mAdapter.getItemCount() < limitSize) { // 第一页如果不够一页就不显示没有更多数据布局
                            mAdapter.loadMoreEnd(true);
                        }
                    }
                } else {
                    mAdapter.loadMoreFail(); // 获取更多数据失败，点击重试
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvGrowDetail.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() { // 加载更多
            @Override
            public void onLoadMoreRequested() {
                getData(TgConstantGetDataType.LOAD_MORE);
            }
        }, rvGrowDetail);
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvGrowDetail.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvGrowDetail.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
