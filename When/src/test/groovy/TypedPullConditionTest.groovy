import houtbecke.rs.when.PullCondition
import houtbecke.rs.when.TypedPullCondition

class TypedPullConditionTest extends groovy.util.GroovyTestCase {

    class SimplePullCondition extends TypedPullCondition {

        boolean isMet() {
            true
        }
    }

    class MultiTypedPullCondition extends TypedPullCondition {

        boolean isMet(String bla) {
            true
        }

        boolean isMet(Integer i) {
            false
        }
    }

    void testSimplePullCondition() {
        def simpleCondition = (PullCondition) new SimplePullCondition()

        assert simpleCondition.isMet(null)
        assert simpleCondition.isMet(new Object())
        assert simpleCondition.isMet("test" as Object)
    }

    void testMultiTypedPullCondition() {
        def multiCondition = (PullCondition) new MultiTypedPullCondition();
        assert !multiCondition.isMet(null)
        assert !multiCondition.isMet(1 as Object)
        assert multiCondition.isMet("merp" as Object)

    }

    interface A { }; class B implements A {  }

    class InterfaceATypedPullCondition extends TypedPullCondition {

        boolean isMet(A a) {
            true
        }
    }

    void testInterfaces() {
        def interfaces = new InterfaceATypedPullCondition()
        Object o = new B()
        assert interfaces.isMet(o as Object)
    }

}
