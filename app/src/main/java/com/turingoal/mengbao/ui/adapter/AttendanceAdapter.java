package com.turingoal.mengbao.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.R;
import com.turingoal.mengbao.common.biz.domain.Attendance;
import com.turingoal.mengbao.common.constants.TgConstantAttendanceType;

/**
 * 考勤记录adapter
 */
public class AttendanceAdapter extends BaseQuickAdapter<Attendance, TgBaseViewHolder> {
    public AttendanceAdapter() {
        super(R.layout.item_attendance);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Attendance attendance) {
        helper.setText(R.id.tvTitle, TgConstantAttendanceType.getUserTypeStr(attendance.getType()))
                .setText(R.id.tvDate, attendance.getCreateTimeStr4DateTime())
                .setText(R.id.tvContent, attendance.getRemakes())
                .setBackgroundRes(R.id.llAttendance, getAttendanceColor(attendance.getType()));
    }

    /**
     * 不同考勤类型，不同背景色
     */
    private int getAttendanceColor(final int type) {
        if (TgConstantAttendanceType.LATE == type) {
            return R.color.attendance1;
        } else if (TgConstantAttendanceType.LEAVE_EARLY == type) {
            return R.color.attendance2;
        } else if (TgConstantAttendanceType.LEAVE == type) {
            return R.color.attendance3;
        } else if (TgConstantAttendanceType.ABSENTEEISM == type) {
            return R.color.attendance4;
        } else {
            return R.color.colorPrimary;
        }
    }
}
