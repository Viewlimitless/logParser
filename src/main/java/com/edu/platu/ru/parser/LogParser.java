package com.edu.platu.ru.parser;

import com.edu.platu.ru.parser.entity.LogLineEntity;
import com.edu.platu.ru.parser.entity.PeriodEntity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParser {
    private final String logDir;
    private final int procTimeLimit;
    private final float ratioLimit;

    public LogParser(String logDir, int procTimeLimit, float ratioLimit) {
        this.logDir = logDir;
        this.procTimeLimit = procTimeLimit;
        this.ratioLimit = ratioLimit;
    }

    //загружаем все строки логов в список
    public Stream<String> loadStreamOfLogsFromFiles() {
        try {
            return Files.lines(Paths.get(logDir), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stream<LogLineEntity> toLogLineEntity(Stream<String> stream) {
        return stream.map(e -> LogLineEntity.parseToEntitiy(e, procTimeLimit));
    }

    public Stream<PeriodEntity> toPeriodEntity(Stream<LogLineEntity> stream) {
        PeriodEntity.init();
        int initialCapacity = getPeriodInitialCapacity();
        ArrayList<LogLineEntity> logLineEntities = new ArrayList<>(initialCapacity);
        return stream.peek(entity -> logLineEntities.add(entity))
                .filter(logLineEntity -> logLineEntities.size() == initialCapacity)
                .map(e -> PeriodEntity.toPeriod(logLineEntities))
                .peek(e -> logLineEntities.clear());
    }

    public Stream<PeriodEntity> concatPeriod(Stream<PeriodEntity> stream) {
        return stream
                .peek(period -> period.tryСoncat(ratioLimit));
    }

    public List<PeriodEntity> toList(Stream<PeriodEntity> stream) {
        return stream.collect(Collectors.toList());
    }

    public void handle() {
        PeriodEntity.init();
        printNoConcated(toList(concatPeriod(toPeriodEntity(toLogLineEntity(loadStreamOfLogsFromFiles())))));
    }

    private int getPeriodInitialCapacity() {
        return (int) (100 / (100 - ratioLimit));
    }

    private void printNoConcated(List<PeriodEntity> list) {
        list.stream().filter(period -> !period.isConcated())
                .filter(period -> period.getRejectionRate() <= ratioLimit)
                .forEach(System.out::println);
    }


}