package houtbecke.rs.when.robo.act;

import javax.inject.Singleton;

import houtbecke.rs.when.robo.BusPushCondition;
import houtbecke.rs.when.robo.OnUiThread;

@Singleton
public class RegisterToBus extends AndroidTypedAct {

    @OnUiThread
    public void act(BusPushCondition... busPushConditions) {
        for (BusPushCondition busPushCondition : busPushConditions) {
            busPushCondition.register();
        }
    }
}
