package com.spark.breakabletoy.model;

import java.time.Instant;

public class Task {
    private int id;
    private String task;
    private String desc;
    private int priority; // LOW:0 - MEDIUM:1 - HIGH:2
    private String creation;
    private String deadline; // YYYY - MM - DD Format
    private String finDate; // YYYY - MM - DD Format
    private boolean fin;

    public Task(){

    }

    public Task(String title, String inDesc, int inPrio, String inDeadL){
        this.task = title;
        this.desc = inDesc;
        this.priority = inPrio;
        this.creation = Instant.now().toString();
        this.deadline = inDeadL;
        this.fin = false;
    }

    public Task(int inID, String title, String inDesc, int inPrio, String inDeadL, boolean inFin, String inFinD){
        this.id = inID;
        this.task = title;
        this.desc = inDesc;
        this.priority = inPrio;
        this.creation = Instant.now().toString();
        this.deadline = inDeadL;
        this.fin = inFin;
        this.finDate = inFinD;
    }

    public int getID(){ return this.id; }
    public void setID(int newID){ this.id = newID; }

    public String getTitle(){ return this.task; }
    public void setTitle(String newTask){ this.task = newTask; }

    public String getDesc(){ return this.desc; }
    public void setDesc(String newDesc){ this.desc = newDesc; }

    public int getPriority(){ return this.priority; }
    public void setPriority(int newPriority){ this.priority = newPriority; }

    public String getCreation(){ return this.creation;}
    public void setCreation(String newCreation) { this.creation = newCreation;}

    public String getDeadline(){ return this.deadline; }
    public void setDeadline(String newDeadline){ this.deadline = newDeadline; }

    public String getFinDate(){ return this.finDate; }
    public void setFinDate(String newFinDate){ this.finDate = newFinDate; }

    public boolean getFin(){ return this.fin; }
    public void setFin(boolean newFin){ this.fin = newFin; }
}