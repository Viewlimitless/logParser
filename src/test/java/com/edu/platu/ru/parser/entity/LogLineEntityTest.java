package com.edu.platu.ru.parser.entity;

import com.edu.platu.ru.parser.LogParser;
import com.edu.platu.ru.parser.LogParserTest;
import com.edu.platu.ru.parser.entity.LogLineEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class LogLineEntityTest {

    static LogParser logParser;

    @Before
    public void setUp() throws Exception {
        logParser = new LogParser(LogParserTest.ACCESS_1, 45, 20);
    }

    @Test
    public void parseToEntitiy() {
        LogLineEntity logLineEntity = LogLineEntity.parseToEntitiy(getLogLine(2), 40);
        assertNotNull(logLineEntity);
        assertNotNull(logLineEntity.getDate());
    }

    private String getLogLine(int number) {
        return logParser.loadStreamOfLogsFromFiles().collect(Collectors.toList()).get(number);
    }
}