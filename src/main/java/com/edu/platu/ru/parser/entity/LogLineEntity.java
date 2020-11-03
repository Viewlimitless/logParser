package com.edu.platu.ru.parser.entity;

import com.edu.platu.ru.parser.ProjectUtils;

import java.util.Date;

public class LogLineEntity {
    private Date date;
    private boolean isBad;

    public LogLineEntity(Date date, boolean isBad) {
        this.date = date;
        this.isBad = isBad;
    }

    public static LogLineEntity parseToEntitiy(String line, int respTimeLimit) {
        return new LogLineEntity(ProjectUtils.getDate(line.split(" ")[3].replaceAll("\\[", ""))
                , ProjectUtils.isBadRespCodeOrRespTime(line, respTimeLimit));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isBad() {
        return isBad;
    }

    public void setBad(boolean bad) {
        isBad = bad;
    }

    @Override
    public String toString() {
        return ProjectUtils.getTime(date) + " isBad: " + isBad;
    }
}
