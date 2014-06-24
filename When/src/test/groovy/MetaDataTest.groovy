import houtbecke.rs.when.GeneralAct
import houtbecke.rs.when.HashMapMetaDataStore
import houtbecke.rs.when.MetaData
import houtbecke.rs.when.MetaDataListener
import houtbecke.rs.when.PullCondition
import houtbecke.rs.when.SimpleAct
import houtbecke.rs.when.W
import houtbecke.rs.when.act.StoreMetaData
import houtbecke.rs.when.condition.HasMetaData
import houtbecke.rs.when.condition.MetaDataStored;

public class MetaDataTest extends GroovyTestCase {

    def object1 = new Object()
    def object2 = new Object()

    def listenerObject1 = new MetaDataListener() {
        def called = 0;
        MetaData stored;
        @Override
        void metaDataSet(MetaData metaData) {
            called++;
            stored = metaData;

        }
    }

    def listenerAll = new MetaDataListener() {
        def called = 0;
        MetaData stored;
        @Override
        void metaDataSet(MetaData metaData) {
            called++;
            stored = metaData;

        }
    }


    void testMetaData() {
        def store = new HashMapMetaDataStore<String>()

        def merp = new MetaData(object1, "merp", "derp")
        def herp = new MetaData(object2, "herp", "derp")


        store.addMetaDataListener(object1, listenerObject1)
        store.addMetaDataListener(listenerAll)


        store.storeMetaData(merp)

        assert listenerObject1.called == 1
        assert listenerObject1.stored == merp

        assert listenerAll.called == 1
        assert listenerAll.stored == merp

        store.storeMetaData(herp)

        assert listenerObject1.called == 1
        assert listenerAll.called == 2
        assert listenerAll.stored == herp

    }

    enum Fields {
        MERP,
        HERP
    }



    void testMetaDataCondition() {
        def merp = new MetaData<Fields>(object1, Fields.MERP, "derp")
        def herp = new MetaData<Fields>(object1, Fields.HERP, "derp")
        def herp2 = new MetaData<Fields>(object2, Fields.HERP, "derp")
        def merp2 = new MetaData<Fields>(object2, Fields.MERP, "merpie")


        def store = new HashMapMetaDataStore<>()
        def merpStored = new MetaDataStored(Fields.MERP)

        def wasStored = false
        def wasStored2 = false

        W.hen(object1, store).is(merpStored).then(new GeneralAct() {
            @Override
            void act() {
                wasStored = true
            }
        }).work()

        def anyFieldStored = new MetaDataStored()

        W.hen(object2, store).is(anyFieldStored).then(new GeneralAct() {
            @Override
            void act() {
                wasStored2 = true
            }
        }).work();


        store.storeMetaData(merp)
        assert wasStored && !wasStored2
        wasStored = false;
        store.storeMetaData(herp)
        assert !wasStored && !wasStored2
        wasStored = false

        store.storeMetaData(merp2)
        assert wasStored2 && !wasStored
        wasStored2 = false
        store.storeMetaData(herp2)
        assert wasStored2 && !wasStored

    }

    void testMetaDataAct() {
        def store = new HashMapMetaDataStore<String>()

        def merp = new MetaData(object1, "merp", "derp")

        def storeMetaData = new StoreMetaData()

        W.hen(new PullCondition() {
            @Override
            boolean isMet(Object thing) {
                return true;
            }
        }).then(storeMetaData, store, merp)
        .work()

        assert store.getMetaData(object1, "merp") == "derp"

    }

    void testHasMetaData() {
        def object1 = new Integer(1);
        def object2 = new Integer(2);
        def store = new HashMapMetaDataStore<String>()
        def hasMetaData = new HasMetaData<Integer, String>(Integer.class, store, "merp", "derp");

        assert !hasMetaData.isMet(object1)

        store.storeMetaData(new MetaData<String>(object2, "merp", "derp"));

        assert !hasMetaData.isMet(object1)

        store.storeMetaData(new MetaData<String>(object1, "merp", "derp"));

        assert hasMetaData.isMet(object1)
    }
}
