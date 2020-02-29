package me.border.arenatimeout.utils;

public enum TimeoutUnit {
    SECOND("s", 1/60), MINUTE("m", 1), HOUR("h", 60), DAY("d", 60*24), WEEK("w", 60*24*7), MONTH("mo", 30*60*24), YEAR("y", 30*60*24*12);

    public String name;
    public int multi;

    TimeoutUnit(String n, int mult){
        name = n;
        multi = mult;
    }

    public static long getTicks(String un, int time){
        long sec;

        try
        {
            sec = time * 60;
        }catch(NumberFormatException ex){
            return 0;
        }

        for(TimeoutUnit unit: TimeoutUnit.values()){
            if(un.startsWith(unit.name)){
                return (sec *= unit.multi)*1000;
            }
        }

        return 0;
    }
}