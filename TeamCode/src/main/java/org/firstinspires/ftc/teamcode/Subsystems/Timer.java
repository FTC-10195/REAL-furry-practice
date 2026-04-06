package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Timer {
    long timeSnapshot;
    long waitTime = 0;
    public static Timer timer = new Timer();
    public Timer(){
        this.waitTime = 0;
        timeSnapshot = System.currentTimeMillis();
    }
    public Timer(long waitTime){
        this.waitTime = waitTime;
        timeSnapshot = System.currentTimeMillis();
    }
    public void setWaitTime(long waitTime){
        this.waitTime = waitTime;
        timeSnapshot = System.currentTimeMillis();
    }
    public long getStartTime(){ return timeSnapshot;}
    public long getWaitTime() {return waitTime;}
    public long getTimePassed() {return System.currentTimeMillis() - timeSnapshot;}
    public boolean doneWaiting() { return System.currentTimeMillis() - timeSnapshot > waitTime;}
}
