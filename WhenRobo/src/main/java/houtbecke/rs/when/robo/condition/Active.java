package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Active extends BaseActive {

    @Inject
    public Active(Bus bus) {
        super(bus, true, false);
    }
}
