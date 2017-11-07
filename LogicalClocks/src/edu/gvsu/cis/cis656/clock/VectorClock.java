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
        JSONObject currentClock = new JSONObject(this.clock);
        JSONObject newClock = new JSONObject(other.toString());
        for(String key:newClock.keySet()){
            if (!currentClock.has(key)){
                this.clock.put(key, newClock.getInt(key));
            }else if (this.clock.get(key) < newClock.getInt(key)){
                this.clock.put(key, newClock.getInt(key));
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
        JSONObject jsonObject = new JSONObject(this.clock);
        Boolean happenedBefore = true;
        for(String key: jsonObject.keySet()){
            if(jsonObject.getInt(key) > other.getTime(Integer.parseInt(key))){
                happenedBefore = false;
                break;
            }
        }
        return happenedBefore;
    }

    public String toString() {
        JSONObject jsonObject = new JSONObject(this.clock);
        return jsonObject.toString();
    }

    @Override
    public void setClockFromString(String clock) {
        Boolean readyToSet = true;
        JSONObject jsonObject = new JSONObject(clock);
        for(String key: jsonObject.keySet()){
            if(!(jsonObject.get(key) instanceof Integer)){
                readyToSet = false;
                break;
            }
        }
        if(!readyToSet){
            return;
        }
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
        JSONObject jsonObject = new JSONObject(this.clock);
        this.clock.put(String.valueOf(p),c);
    }
}
