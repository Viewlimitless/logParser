package com.edu.platu.ru.parser;

import com.edu.platu.ru.parser.entity.LogLineEntity;
import com.edu.platu.ru.parser.entity.PeriodEntity;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class LogParserTest {
    public static final String PATH = "src/test/logs/";
    public static final String ACCESS = PATH + "access.log";
    public static final String ACCESS_1 = PATH + "access1.log";
    public static final String ACCESS_2 = PATH + "access2.log";

    public static LogParser logParser1;
    public static LogParser logParser2;

    @BeforeClass
    public static void setUpSpec() throws Exception {

        logParser1 = new LogParser(ACCESS_1, 45, 20f);
        logParser2 = new LogParser(ACCESS_2, 45, 98.99f);
    }

    @Before
    public void setUp() throws Exception {
        PeriodEntity.init();
    }

    @Test
    public void loadStreamOfLogsFromFiles() {
        List<String> list = logParser1.loadStreamOfLogsFromFiles().collect(Collectors.toList());
        logHelper(list);
        assertTrue(10 == list.size());
    }

    @Test
    public void toLogLineEntity() {
        List<LogLineEntity> list = logParser1.toLogLineEntity(logParser1.loadStreamOfLogsFromFiles()).collect(Collectors.toList());
        logHelper(list);
        assertTrue(10 == list.size());
    }

    @Test
    public void toPeriodEntity() {
        List<PeriodEntity> list = logParser2.toPeriodEntity(logParser2.toLogLineEntity(logParser2.loadStreamOfLogsFromFiles())).collect(Collectors.toList());
        logHelper(list);
        assertTrue(5 == list.size());
    }

    @Test
    public void concatPeriod() {
        List<PeriodEntity> list = logParser2.concatPeriod(logParser2.toPeriodEntity(logParser2.toLogLineEntity(logParser2.loadStreamOfLogsFromFiles()))).collect(Collectors.toList());
        logHelper(list);
        assertTrue(5 == list.size());
    }

    private <T> void logHelper(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
    }

    @Test
    public void toList() {
        List<PeriodEntity> list = logParser2.toList(logParser2.concatPeriod(logParser2.toPeriodEntity(logParser2.toLogLineEntity(logParser2.loadStreamOfLogsFromFiles()))));
        logHelper(list);
        assertTrue(list.size() == 5);
    }

    @Test
    public void handle() {
        LogParser logParser = new LogParser(ACCESS, 45, 86.2f);
        logParser.handle();
    }

}