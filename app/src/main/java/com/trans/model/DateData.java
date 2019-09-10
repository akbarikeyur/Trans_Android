package com.trans.model;

import com.trans.global.Utility;

public class DateData {
    String date, dateOrigional;
    boolean selected, current;
    JourneyData journeyData;
    long dateMilliSecond;

    public DateData(String dateOrigional, boolean selected) {
        this.dateOrigional = dateOrigional;
        this.dateMilliSecond = Utility.milliseconds(dateOrigional);
        this.selected = selected;
        this.current = false;
        this.date = Utility.getDepartProceedFormat(dateOrigional);
    }

    public DateData(String dateOrigional, boolean selected, boolean current) {
        this.dateOrigional = dateOrigional;
        this.dateMilliSecond = Utility.milliseconds(dateOrigional);
        this.date = Utility.getDepartProceedFormat(dateOrigional);
        this.selected = selected;
        this.current = current;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getDateOrigional() {
        return dateOrigional;
    }

    public void setDateOrigional(String dateOrigional) {
        this.dateOrigional = dateOrigional;
    }

    public long getDateMilliSecond() {
        return dateMilliSecond;
    }

    public void setDateMilliSecond(long dateMilliSecond) {
        this.dateMilliSecond = dateMilliSecond;
    }

    public JourneyData getJourneyData() {
        return journeyData;
    }

    public void setJourneyData(JourneyData journeyData) {
        this.journeyData = journeyData;
    }

}
