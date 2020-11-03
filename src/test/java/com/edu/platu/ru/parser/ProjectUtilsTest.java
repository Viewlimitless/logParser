package com.edu.platu.ru.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProjectUtilsTest {

    static LogParser logParser;

    @Before
    public void setUp() throws Exception {
        logParser  = new LogParser(LogParserTest.ACCESS_1, 45, 20);
    }

    @Test
    public void getDate() {
    }

    @Test
    public void isBadRecpCode() {
//        192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1" 200 2 44.510983 "-" "@list-item-updater" prio:0
        assertFalse(ProjectUtils.isBadRespCode(getLogLine(0)));
//        192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1" 500 2 23.251219 "-" "@list-item-updater" prio:0
        assertTrue(ProjectUtils.isBadRespCode(getLogLine(1)));
    }

    @Test
    public void isBadRespTime() {
//        192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=e356713 HTTP/1.1" 200 2 30.164372 "-" "@list-item-updater" prio:0
        assertTrue(ProjectUtils.isBadRespTime(getLogLine(2), 29));
        assertTrue(ProjectUtils.isBadRespTime(getLogLine(2), 30));
        assertFalse(ProjectUtils.isBadRespTime(getLogLine(2), 31));
    }

    @Test
    public void isBadRespCodeOrRespTime() {
//        192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1" 200 2 44.510983 "-" "@list-item-updater" prio:0
        assertTrue(ProjectUtils.isBadRespCodeOrRespTime(getLogLine(0), 44));
        assertFalse(ProjectUtils.isBadRespCodeOrRespTime(getLogLine(0), 45));
//        192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1" 500 2 23.251219 "-" "@list-item-updater" prio:0
        assertTrue(ProjectUtils.isBadRespCodeOrRespTime(getLogLine(1), 23));
        assertTrue(ProjectUtils.isBadRespCodeOrRespTime(getLogLine(1), 24));
    }

    private String getLogLine(int number){
        return logParser.loadStreamOfLogsFromFiles().collect(Collectors.toList()).get(number);
    }
}