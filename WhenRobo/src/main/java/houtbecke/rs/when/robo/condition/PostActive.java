package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PostActive extends BaseActive {

    @Inject
    public PostActive(Bus bus) {
        super(bus, true, false, false, false);
    }
}
