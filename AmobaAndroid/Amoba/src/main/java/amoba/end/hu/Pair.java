package amoba.end.hu;

import java.util.TreeMap;

public class Pair<Key,Value> implements TreeMap.Entry<Key,Value>{
    protected Key key;
    protected Value value;

    public Pair(Key key, Value value){
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public Value setValue(Value value) {
        this.value = value;
        return value;
    }
}
