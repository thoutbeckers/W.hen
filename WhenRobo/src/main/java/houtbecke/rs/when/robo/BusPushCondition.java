package houtbecke.rs.when.robo;


import com.squareup.otto.Bus;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;

public abstract class BusPushCondition extends BasePushCondition {

    boolean registered =false;

    Bus bus;

    @Inject
    public BusPushCondition(Bus bus){
        this.bus = bus;
    }


    public synchronized void register(){
        if (!registered){
            bus.register(this);
            registered = true;
        }
    }

    public synchronized void unregister(){
        if (registered){
            bus.unregister(this);
            registered = false;
        }
    }

}
