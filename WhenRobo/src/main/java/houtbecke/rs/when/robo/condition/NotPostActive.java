package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotPostActive extends BaseActive {

    @Inject
    public NotPostActive(Bus bus) {
        super(bus, false, false, false, true);
    }

}
