package edu.gvsu.cis.cis656.clock;

import java.util.Hashtable;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class VectorClock implements Clock {

    // suggested data structure ...
    private Map<String,Integer> clock = new Hashtable<String,Integer>();


    @Override
    public void update(Clock other) {
        JSONObject obj = new JSONObject(other.toString());
        for(String key:obj.keySet()){
            if (this.clock.get(key) < obj.getInt(key)){
                this.clock.put(key, obj.getInt(key));
            }
        }
    }

    @Override
    public void setClock(Clock other) {

        JSONObject obj = new JSONObject(other.toString());
        for(String key:obj.keySet()){
            if (!(obj.get(key) instanceof Integer)){
                return;
            }
        }
        this.clock.clear();
        for(String key:obj.keySet()){
            this.clock.put(key,obj.getInt(key));
        }
    }

    @Override
    public void tick(Integer pid) {
        String key = String.valueOf(pid);
        int currentValue = this.clock.get(key);
        this.clock.put(key, currentValue + 1);
    }

    @Override
    public boolean happenedBefore(Clock other) {

        return false;
    }

    public String toString() {
        JSONObject jsonObject = new JSONObject(this.clock);
        return jsonObject.toString();
    }

    @Override
    public void setClockFromString(String clock) {
        JSONObject jsonObject = new JSONObject(clock);
        this.clock.clear();
        for(String key:jsonObject.keySet()){
            this.clock.put(key,jsonObject.getInt(key));
        }
    }

    @Override
    public int getTime(int p) {

        if(this.clock.containsKey(String.valueOf(p))){
            return this.clock.get(String.valueOf(p));
        }
        return 0;
    }

    @Override
    public void addProcess(int p, int c) {
        this.clock.put(String.valueOf(p),c);
    }
}
