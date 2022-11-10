package com.bookmydoc.horizontalcalendar.adapter;

import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;

import com.bookmydoc.R;
import com.bookmydoc.horizontalcalendar.HorizontalCalendar;
import com.bookmydoc.horizontalcalendar.HorizontalCalendarView;
import com.bookmydoc.horizontalcalendar.model.HorizontalCalendarConfig;
import com.bookmydoc.horizontalcalendar.utils.CalendarEventsPredicate;
import com.bookmydoc.horizontalcalendar.utils.HorizontalCalendarPredicate;
import com.bookmydoc.horizontalcalendar.utils.Utils;

import java.util.Calendar;
import java.util.List;


public class MonthsAdapter extends HorizontalCalendarBaseAdapter<DateViewHolder, Calendar> {

    public MonthsAdapter(HorizontalCalendar horizontalCalendar, Calendar startDate, Calendar endDate, HorizontalCalendarPredicate disablePredicate, CalendarEventsPredicate eventsPredicate) {
        super(R.layout.hc_item_calendar, horizontalCalendar, startDate, endDate, disablePredicate, eventsPredicate);
    }

    @Override
    protected DateViewHolder createViewHolder(View itemView, int cellWidth) {
        final DateViewHolder holder = new DateViewHolder(itemView);

        //holder.layoutContent.setMinimumWidth(cellWidth);
        //holder.textTop.setVisibility(View.GONE);

        return holder;
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        Calendar month = getItem(position);
        HorizontalCalendarConfig config = horizontalCalendar.getConfig();

        final Integer selectorColor = horizontalCalendar.getConfig().getSelectorColor();
        if (selectorColor != null) {

            holder.layoutContent.setBackground(horizontalCalendar.getContext().getResources().getDrawable(R.drawable.rounded_blue_date));

        }

        holder.textMiddle.setText(DateFormat.format(config.getFormatMiddleText(), month));
        holder.textMiddle.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.getSizeMiddleText());

        if (config.isShowTopText()) {
            holder.textTop.setText(DateFormat.format(config.getFormatTopText(), month));
            holder.textTop.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.getSizeTopText());
        } else {
            holder.textTop.setVisibility(View.GONE);
        }

        if (config.isShowBottomText()) {
            holder.textBottom.setText(DateFormat.format(config.getFormatBottomText(), month));
            holder.textBottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.getSizeBottomText());
        } else {
            holder.textBottom.setVisibility(View.GONE);
        }

        showEvents(holder, month);
        applyStyle(holder, month, position);

    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position, List<Object> payloads) {
        if ((payloads == null) || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        Calendar date = getItem(position);
        applyStyle(holder, date, position);
    }

    @Override
    public Calendar getItem(int position) throws IndexOutOfBoundsException {
        if (position >= itemsCount) {
            throw new IndexOutOfBoundsException();
        }

        int monthsDiff = position - horizontalCalendar.getShiftCells();

        Calendar calendar = (Calendar) startDate.clone();
        calendar.add(Calendar.MONTH, monthsDiff);

        return calendar;
    }

    @Override
    protected int calculateItemsCount(Calendar startDate, Calendar endDate) {
        int days = Utils.monthsBetween(startDate, endDate) + 1;
        return days + (horizontalCalendar.getShiftCells() * 2);
    }

}
