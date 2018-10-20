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
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.common.biz.domain.Message;
import com.turingoal.mengbao.constants.ConstantActivityPath;
import com.turingoal.mengbao.constants.ConstantUrls;
import com.turingoal.mengbao.ui.adapter.HomeworkAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 亲子作业
 */
@Route(path = ConstantActivityPath.HOMEWORK)
public class HomeworkActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvHomework)
    RecyclerView rvHomework;
    private HomeworkAdapter mAdapter = new HomeworkAdapter();
    private int limitSize = 10; // 一次加载多少条数据
    private int pageSize = 2; // 第几页

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework);
        initRecyclerAndAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(TgConstantGetDataType.INIT);
    }

    /**
     * 加载作业数据
     */
    private void getData(final int getDataType) {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK, getHttpTaskKey());
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
                    List<HomeworkRecord> homeworkRecords = TgJsonUtil.jsonResultBean2List(result, HomeworkRecord.class);
                    if (homeworkRecords != null) {
                        if (TgConstantGetDataType.INIT == getDataType) {
                            pageSize = 2;
                            mAdapter.setNewData(homeworkRecords);
                        } else {
                            pageSize++;
                            // 去重
                            List<HomeworkRecord> homeworkRecordList = new ArrayList<>();
                            homeworkRecordList.addAll(homeworkRecords);
                            homeworkRecordList.removeAll(mAdapter.getData());
                            mAdapter.addData(homeworkRecordList);
                        }
                        mAdapter.loadMoreComplete(); // 成功获取更多数据
                        if (homeworkRecords.size() < limitSize) {
                            mAdapter.loadMoreEnd(false); // 加载结束，没有数据，false显示没有更多数据，true不显示任何提示信息
                        }
                        if (mAdapter.getData().size() < limitSize) { // 第一页如果不够一页就不显示没有更多数据布局
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
        rvHomework.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() { // 加载更多
            @Override
            public void onLoadMoreRequested() {
                getData(TgConstantGetDataType.LOAD_MORE);
            }
        }, rvHomework);
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvHomework.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvHomework.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
