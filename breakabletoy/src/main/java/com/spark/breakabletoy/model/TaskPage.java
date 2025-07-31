package com.spark.breakabletoy.model;

import java.util.List;

public class TaskPage<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private int totalElements;
    private long totalAvg;
    private long highAvg;
    private long mediumAvg;
    private long lowAvg;

    public TaskPage(List<T> inContent, int inPage, int inSize, int inTotalPages, int inTotalElements, long inTotalAvg, long inHighAvg, long inMediumAvg, long inLowAvg){
        this.content = inContent;
        this.page = inPage;
        this.size = inSize;
        this.totalPages = inTotalPages;
        this.totalElements = inTotalElements;
        this.totalAvg = inTotalAvg;
        this.highAvg = inHighAvg;
        this.mediumAvg = inMediumAvg;
        this.lowAvg = inLowAvg;
    }

    public void setContent(List<T> inContent) { this.content = inContent; }
    public List<T> getContent() { return this.content; }

    public void setPage(int inPage) { this.page = inPage; }
    public int getPage() { return this.page; }

    public void setSize(int inSize) { this.size = inSize; }
    public int getSize() { return this.size; }

    public void setTotalPages(int inTotalPages) { this.totalPages = inTotalPages; }
    public int getTotalPages() { return this.totalPages; }

    public void setTotalElements(int inTotalElements) { this.totalElements = inTotalElements; }
    public int getTotalElements() { return this.totalElements; }

    public void setTotalAvg(long inTotalAvg) { this.totalAvg = inTotalAvg; }
    public long getTotalAvg() { return this.totalAvg; }

    public void setHighAvg(long inHighAvg) { this.highAvg = inHighAvg; }
    public long getHighAvg() { return this.highAvg; }

    public void setMediumAvg(long inMediumAvg) { this.mediumAvg = inMediumAvg; }
    public long getMediumAvg() { return this.mediumAvg; }

    public void setLowAvg(long inLowAvg) { this.lowAvg = inLowAvg; }
    public long getLowAvg() { return this.lowAvg; }
}