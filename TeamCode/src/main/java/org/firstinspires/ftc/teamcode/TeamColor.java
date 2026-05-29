package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TeamColor {
    public enum Team {
        RED,
        BLUE;
    }
    public static Team currentTeamColor = Team.RED;
    public static void switchTeam(){
        switch (currentTeamColor){
            case RED:
                currentTeamColor = Team.BLUE;
                break;
            case BLUE:
                currentTeamColor = Team.RED;
                break;
        }
    }
    public static void update(Telemetry telemetry){
        telemetry.addData("CurrentTeamColor", currentTeamColor);
    }
}
