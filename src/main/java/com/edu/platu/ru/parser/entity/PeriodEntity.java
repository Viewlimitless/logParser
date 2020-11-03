package com.edu.platu.ru.parser.entity;

import com.edu.platu.ru.parser.ProjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class PeriodEntity {
    private Date startDate;
    private Date endDate;
    private int count;
    private float rejectionRate;
    private static PeriodEntity lastPeriod;
    private PeriodEntity linkToPreviousPeriod;
    private boolean concated = false;

    static {
        init();
    }

    public static void init() {
        //stub never be concated or try concated
        lastPeriod = new PeriodEntity(new Date(), new Date(), 0, 0);
        lastPeriod.setConcated(true);
    }

    public PeriodEntity(Date startDate, Date endDate, int count, float rejectionRate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.count = count;
        this.rejectionRate = rejectionRate;
        this.linkToPreviousPeriod = lastPeriod;
    }

    public static PeriodEntity toPeriod(ArrayList<LogLineEntity> logLineEntities) {
        float rejectionRate = logLineEntities.stream()
                .collect(Collectors.averagingInt(entity -> entity.isBad() ? 0 : 100))
                .floatValue();
        Date startDate = logLineEntities.get(0).getDate();
        Date endDate = logLineEntities.get(logLineEntities.size() - 1).getDate();
        lastPeriod = new PeriodEntity(startDate, endDate, logLineEntities.size(), rejectionRate);
        return lastPeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getRejectionRate() {
        return rejectionRate;
    }

    public void setRejectionRate(float rejectionRate) {
        this.rejectionRate = rejectionRate;
    }

    public PeriodEntity getLinkToPreviousPeriod() {
        return linkToPreviousPeriod;
    }

    public boolean isConcated() {
        return concated;
    }

    public void setConcated(boolean concated) {
        this.concated = concated;
    }

    public void setLinkToPreviousPeriod(PeriodEntity linkToPreviousPeriod) {
        this.linkToPreviousPeriod = linkToPreviousPeriod;
    }

    public void tryСoncat(float ratioLimit) {
        if (!this.isConcated() &&!this.getLinkToPreviousPeriod().isConcated() && canConcatperiods(this, this.getLinkToPreviousPeriod(), ratioLimit)) {
            concat(this, this.getLinkToPreviousPeriod());
        }
    }

    private void concat(PeriodEntity a, PeriodEntity b) {
        a.setRejectionRate((float) ((a.getCount() * a.getRejectionRate() + b.getCount() * b.getRejectionRate()) / (a.getCount() + b.getCount())));
        a.setCount(a.getCount() + b.getCount());
        a.setStartDate(b.getStartDate());
        b.setConcated(true);
    }

    private boolean canConcatperiods(PeriodEntity a, PeriodEntity b, float ratioLimit) {
        return (a.getCount() * a.getRejectionRate() + b.getCount() * b.getRejectionRate()) / (a.getCount() + b.getCount()) < ratioLimit;
    }

    @Override
    public String toString() {
        return ProjectUtils.getTime(startDate) + "\t" + ProjectUtils.getTime(endDate) + "\t" + Math.round(rejectionRate*10) / 10.0;
    }
}
