package com.dapaua.trmwidget;


import java.util.LinkedList;
import java.util.List;

import org.apache.commons.exec.LogOutputStream;

public class CollectOutput extends LogOutputStream {
    private final List lines = new LinkedList();
    @Override protected void processLine(String line, int level) {
        lines.add(line);
    }   
    public List getLines() {
        return lines;
    }
}
