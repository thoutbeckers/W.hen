package houtbecke.rs.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class WhenBuilder implements PushCondition, PullCondition {

    enum Logic {
        OR,
        AND,
        AND_NOT,
        OR_NOT
    }

    enum BuildingState {
        CONDITIONS,
        CONSEQUENCES
    }

    enum BuilderElement {
        START,
        WHEN,
        A,
        IS,
        AND,
        OR,
        NOT,
        THEN,
        WITH,
        ACT,

    }

    final static Condition[] NAC = new Condition[0]; // no additional conditions
    final static Act[] NAA = new Act[0]; // no additional acts
    final static Object[] NAT = new Object[0]; // no additional things

    volatile boolean working = false;

    BuilderElement lastElement = BuilderElement.START;
    BuildingState state = BuildingState.CONDITIONS;

    private class GroupOfConditionalThings implements PushConditionListener {

        List<Object> things = new ArrayList<Object>(1);
        List<Condition> conditions = new ArrayList<Condition>(1);
        IdentityHashMap<Condition, Logic> logic = new IdentityHashMap<Condition, Logic>(0);
        Logic logicForNextGroup = null;

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < things.size(); i++) {
                ret.append("a ");
                Object thing = things.get(i);
                ret.append(thing).append(" ");


                if (i == things.size() -1) continue;
            }

            for (int i = 0; i < conditions.size(); i++) {
                ret.append("is ");
                Condition condition = conditions.get(i);
                ret.append(condition).append(" ");

                if (i == conditions.size() -1) continue;

                Logic l = null;
                for (int k = i; k < conditions.size() && l== null; k++)
                    l = logic.get(conditions.get(k));

                if (l!=null)
                    ret.append(l).append(" ");
            }

            if (logicForNextGroup != null)
                ret.append(logicForNextGroup).append(" ");

            return ret.toString();
        }

        Set<Condition> pushed = new HashSet<Condition>(0);
        Set<Condition> tempChanged = new HashSet<Condition>(1);

        boolean isTriggered(Condition condition) {

            // check if there if this is a condition that is temporarily met
            if (tempChanged.contains(condition))
                return true;

            if (pushed.contains(condition))
                return true;

            return false;
        }


        @Override
        public void push(Condition condition, boolean conditionMet, boolean sticky, Object... results) {

            if (sticky) {
                if (conditionMet)
                    pushed.add(condition);
                else
                    pushed.remove(condition);
            }
            else {
                tempChanged.add(condition);
            }

            if (working) {
                if (evaluateConditions(results)) {
                    doActs(results);
                    for (PushConditionListener t: conditionListeners)
                        t.push(WhenBuilder.this, true, sticky, results);
                }

            }

            tempChanged.clear();
        }


        boolean updateEvaluation(Logic logic, Boolean conditionsEvaluate, boolean isTriggered) {
            if (conditionsEvaluate == null)
                conditionsEvaluate = isTriggered;
            else switch (logic) {
                case OR_NOT:
                    isTriggered = !isTriggered;
                case OR:
                    conditionsEvaluate |= isTriggered;
                    break;

                case AND_NOT:
                    isTriggered = !isTriggered;
                case AND:
                    conditionsEvaluate &= isTriggered;
                    break;

            }
            return conditionsEvaluate;
        }

        boolean evaluateLeftToRight(Object... results) {

            // we will evaluate if things meet their conditions one by one from left to right

            final int sizeConditions = conditions.size();

            Logic conditionsLogic = null;
            int foundConditionsLogic = -1;
            Boolean conditionsEvaluate = null;

            // this loop will be called for each thing or once if there are no things.
            for (int k = 0; k < sizeConditions; k++) {
                Condition condition = conditions.get(k);

                // if we don't know the logic under which we operate try to find it
                if (conditionsLogic == null || k > foundConditionsLogic)
                    for (foundConditionsLogic = k; foundConditionsLogic < sizeConditions; foundConditionsLogic++) {
                        Condition foundCondition = conditions.get(foundConditionsLogic);
                        conditionsLogic = logic.get(foundCondition);
                        if (conditionsLogic != null)
                            break;
                    }


                if (conditionsLogic == null)
                    conditionsLogic = Logic.AND;

                boolean isTriggered = false;

                if (condition instanceof PushCondition)
                    isTriggered = isTriggered(condition);
                if (!isTriggered && condition instanceof PullCondition) {
                    PullCondition pullCondition = (PullCondition) condition;
                    for (Object thing: things) {
                        if (thing instanceof Things) {
                            Collection<Object> subThings = ((Things)thing).getThings();
                            if (subThings != null)
                                for (Object oneOfThings: subThings)
                                    isTriggered |= pullCondition.isMet(oneOfThings);
                        }
                        else
                           isTriggered |= pullCondition.isMet(thing);
                    }

                    for (Object thing: results) {
                        if (thing instanceof Things) {
                            Collection<Object> subThings = ((Things)thing).getThings();
                            if (subThings != null)
                                for (Object oneOfThings: subThings)
                                    isTriggered |= pullCondition.isMet(oneOfThings);
                        }
                        else
                            isTriggered |= pullCondition.isMet(thing);
                    }

                    if (things.size() ==0)
                        isTriggered |= pullCondition.isMet(null);


                }
                conditionsEvaluate = updateEvaluation(conditionsLogic, conditionsEvaluate, isTriggered);


//                evaluates = updateEvaluation(conditionsLogic)
//
//                // we now know if our thing meets the conditions, or if there are no things if the needed conditions are met
//                if (evaluates == null)
//                    evaluates = conditionsEvaluate;
//                else switch (conditionsLogic) {
//                    case OR_NOT:
//                        conditionsEvaluate = !conditionsEvaluate;
//                    case OR:
//                        evaluates |= conditionsEvaluate;
//                        break;
//
//                    case AND_NOT:
//                        conditionsEvaluate = !conditionsEvaluate;
//                    case AND:
//                        evaluates &= conditionsEvaluate;
//                        break;
//
//                }
            }
            return conditionsEvaluate;
        }

    }
    
    private class GroupOfActsForThings {
        List<Object> things = new ArrayList<Object>(1);
        List<Act> acts = new ArrayList<Act>(1);

        boolean useConditionsAsThings = true;
        boolean useConditionalThingsAsThings = true;
        boolean useResultsAsThings = true;

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            if (things.size() > 0) {
                ret.append("with ");
                for (Object thing: things)
                    ret.append(thing).append(" ");
                ret.append(", ");
            }
            for (Act act: acts)
                ret.append(act).append(" ");

            return ret.toString();
        }

        Object[] gatherObjects(Object[] results) {
            List<Object> objects = new ArrayList<Object>(0);
            if (useConditionalThingsAsThings || useConditionsAsThings) {
                for (GroupOfConditionalThings group: conditionsGroups) {
                    if (useConditionalThingsAsThings)
                        objects.addAll(group.things);
                    if (useConditionsAsThings)
                        objects.addAll(group.conditions);
                }
            }
            if (useResultsAsThings && results != null)
                objects.addAll(Arrays.asList(results));

            for(Object thing: things)
                if (thing instanceof Things) {
                    Collection<Object> subThings = ((Things)thing).getThings();
                    if (subThings != null)
                            objects.addAll(subThings);
                }
                else
                    objects.add(thing);

            return objects.toArray();
        }

        void act(Object[] results) {
            Object[] things = gatherObjects(results);
            for (Act act: acts) {
                act.act(things);
            }
        }
    }

    Object currentThing;
    Condition currentCondition;

    List<GroupOfConditionalThings> conditionsGroups = new ArrayList<GroupOfConditionalThings>(1);
    GroupOfConditionalThings currentConditions;
    List<GroupOfActsForThings> actsGroups = new ArrayList<GroupOfActsForThings>(1);
    GroupOfActsForThings currentActs;

    public boolean evaluateConditions(Object... results) {
        boolean evaluates = true;
        Logic logic = Logic.AND;
        for (GroupOfConditionalThings conditionsGroup: conditionsGroups) {
            boolean groupEvaluates = conditionsGroup.evaluateLeftToRight(results);

            switch(logic) {
                case AND_NOT:
                    groupEvaluates = !groupEvaluates;
                case AND:
                    evaluates &= groupEvaluates;
                    break;
                case OR_NOT:
                    groupEvaluates = !groupEvaluates;
                case OR:
                    evaluates |= groupEvaluates;
                    break;
            }

            logic = conditionsGroup.logicForNextGroup;
        }
        return evaluates;
    }

    @Override
    public boolean isMet(Object thing) {
        return evaluateConditions();
    }


    void doActs(Object... results) {
        for (GroupOfActsForThings groupOfActsForThings: actsGroups) {
            groupOfActsForThings.act(results);
        }
    }

    private void nextConditions() {
        if (currentConditions != null && currentConditions.logicForNextGroup == null) {
            if (currentCondition != null)
                currentConditions.logicForNextGroup = currentConditions.logic.remove(currentCondition);
            else if (currentThing != null) {
                currentConditions.logicForNextGroup = currentConditions.logic.remove(currentThing);
            }
        }

        // TODO detect when there is a previous when() group with nothing in it

        currentConditions = new GroupOfConditionalThings();
        conditionsGroups.add(currentConditions);

    }

    private void nextActs() {
        currentActs = new GroupOfActsForThings();
        actsGroups.add(currentActs);
    }

    private void addThingForCondition(Object thing) {
        currentThing = thing;
        currentCondition = null;
        currentConditions.things.add(currentThing);
    }

    private void addThingForAct(Object thing) {
        currentThing = thing;
        currentCondition = null;
        currentActs.things.add(thing);
    }

    public WhenBuilder when() {
        if (state == BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("Can not add when() after then()");
        switch(lastElement) {
            case START:
            case AND:
            case OR:
            case NOT:
                break;
            default:
                // TODO: maybe support in future: when(x).is(y).when(a).is(b).and().when(k).is(l)
                throw new IllegalArgumentException("when() can only be used after an invocation of or() or and() or and().not() or or().not() without arguments ");
        }
        nextConditions();
        lastElement = BuilderElement.WHEN;
        return this;
    }

    public WhenBuilder() {
        when();
    }

    public WhenBuilder(Object thing) { this(thing, NAT); }
    public WhenBuilder(Object thing, Object... moreThings) { when(thing, moreThings); }
    public WhenBuilder when(Object thing) { return when(thing, NAT); }
    public WhenBuilder when(Object thing, Object...  moreThings) {
        when();
        a(thing, moreThings);
        return this;
    }

    public WhenBuilder(Object thing, Condition condition) { this(thing, condition, NAC); }
    public WhenBuilder(Object thing, Condition condition, Condition... moreConditions) { when(thing, condition, moreConditions); }
    public WhenBuilder when(Object thing, Condition condition) { return when(thing, condition, NAC); }
    public WhenBuilder when(Object thing, Condition condition, Condition... moreConditions) {
        when(thing);
        is(condition, moreConditions);
        return this;
    }

    public WhenBuilder(Condition condition) { this(condition, NAC); }
    public WhenBuilder(Condition condition, Condition condition2) { this(condition, new Condition[]{condition2});}
    public WhenBuilder(Condition condition, Condition... moreConditions) { when(condition, moreConditions); }
    public WhenBuilder when(Condition condition) { return when(condition, NAC); }
    public WhenBuilder when(Condition condition, Condition condition2) { return when(condition, new Condition[]{condition2});}
    public WhenBuilder when(Condition condition, Condition... moreConditions) {
        when();
        is(condition, moreConditions);
        return this;
    }


    public WhenBuilder(Condition condition, Act act) { this(condition, act, NAA); }
    public WhenBuilder(Condition condition, Act act, Act... moreActs) {
        this(condition);
        act(act, moreActs);
        work();
    }
    public WhenBuilder when(Condition condition, Act act) { return when(condition, act, NAA); }
    public WhenBuilder when(Condition condition, Act act, Act... moreActs) {
        when(condition);
        then();
        act(act, moreActs);
        work();
        return this;
    }

    public WhenBuilder(Object thing, Condition condition, Act act) { when(thing, condition, act); }
    public WhenBuilder when(Object thing, Condition condition, Act act) {
        when(thing);
        is(condition);
        then();
        act(act);
        work();
        return this;
    }

    public WhenBuilder(Object thing, Condition condition, Object withThing, Act act) { when(thing, condition, withThing, act); }
    public WhenBuilder when(Object thing, Condition condition, Object withThing, Act act) {
        when(thing);
        is(condition);
        then();
        with(withThing);
        act(act);
        work();
        return this;
    }

    void addCondition(Condition condition) {
        currentCondition = condition;
        currentThing = null;
        currentConditions.conditions.add(currentCondition);

        if (condition instanceof PushCondition) {
            PushCondition pushCondition = (PushCondition) condition;
            if (currentConditions.things.isEmpty())
                pushCondition.addListener(currentConditions, null);
            else for (Object thing: currentConditions.things) {
                if (thing instanceof ConditionThings)
                    ((ConditionThings) thing).addPushCondition(pushCondition, currentConditions);
                else
                    pushCondition.addListener(currentConditions, thing);
            }
        }
        if (condition instanceof WhenBuilder)
            ((WhenBuilder) condition).work();
    }

    void addAct(Act act) {
        currentActs.acts.add(act);
    }

    public WhenBuilder an(Object thing, Object...  moreThings) { return a(thing, moreThings); }
    public WhenBuilder the(Object thing, Object...  moreThings) {return a(thing, moreThings); }
    public WhenBuilder an(Object thing) { return a(thing, NAT); }
    public WhenBuilder the(Object thing) { return a(thing, NAT); }
    public WhenBuilder a(Object thing) { return a(thing, NAT); }
    public WhenBuilder a(Object thing, Object...  moreThings) {
        // in CONSEQUENCES a(),an(),the() are aliases for with()
        if (state == BuildingState.CONSEQUENCES)
            return with(thing, moreThings);

        addThingForCondition(thing);
        for (Object t: moreThings) {
            addThingForCondition(t);
        }
        lastElement = BuilderElement.A;
        return this;
    }

    public WhenBuilder are(Condition condition, Condition... moreConditions) { return is(condition, moreConditions); }
    public WhenBuilder are(Condition condition) { return is(condition, NAC); }
    public WhenBuilder is(Condition condition) { return is(condition, NAC); }
    public WhenBuilder is(Condition condition, Condition... moreConditions) {
        if (state == BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("no use of is() or are() after then()");

        Logic logicToAdd = null;

        switch (lastElement) {
            case IS:
                // TODO: maybe create special handling for when().a(thing).is(doingsomehing).is(doingwhatever).or().is(doingsomethingelse)
                throw new IllegalArgumentException("no use of is() or are() after is() or  are(). Add and() or or() first.");
            case AND:
            case OR:
            case NOT:
                logicToAdd = currentConditions.logic.get(currentCondition);
                break;

            default:
        }

        addCondition(condition);
        for (Condition c: moreConditions)
            addCondition(c);

        if (logicToAdd != null) {
            currentConditions.logic.put(currentCondition, logicToAdd);
        }

        lastElement = BuilderElement.IS;
        return this;
    }

    public WhenBuilder are() { return is(); }

    public WhenBuilder is() {
        if (state == BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("no use of is() or are() after then()");

        // never does anything, should be followed by not
        return this;
    }

    public WhenBuilder and() {
        if (state == BuildingState.CONDITIONS) {
            switch(lastElement) {
                case NOT:
                    currentConditions.logic.put(currentCondition, Logic.AND_NOT);
                    break;
                case IS:
                    currentConditions.logic.put(currentCondition, Logic.AND);
                    break;
                default:
                    throw new IllegalArgumentException("can only put and() in a when() section or after a(), an() is() or are() or a statement with a thing or condition as parameter");

            }
        } // else if we are in CONSEQUENCES acts and things will add up by default TODO enforce illegal use anyway such as and().or();


        lastElement = BuilderElement.AND;
        return this;
    }

    public WhenBuilder and(Object thing) { return and(thing, NAT); }
    public WhenBuilder and(Object thing, Object...  moreThings) {
        if (currentThing == null)
            throw new IllegalArgumentException("Can not use and() without adding an object of the same type");
        and();
        a(thing, moreThings);
        return this;
    }

    public WhenBuilder and(Condition condition) { return and(condition, NAC); }
    public WhenBuilder and(Condition condition, Condition... moreConditions) {
        if (currentCondition == null)
            throw new IllegalArgumentException("Can not use and() without adding an object of the same type");
        addCondition(condition);
        for (Condition c: moreConditions) {
            addCondition(c);
        }
        currentConditions.logic.put(currentCondition, Logic.AND);
        lastElement = BuilderElement.IS; // essentially we're a a special case of and().is(condition).
        return this;
    }

    public WhenBuilder or(Condition condition) { return or(condition, NAC); }
    public WhenBuilder or(Condition condition, Condition... moreConditions) {
        if (currentCondition == null)
            throw new IllegalArgumentException("Can not use or() without adding an object of the same type");
        addCondition(condition);
        for (Condition c: moreConditions) {
            addCondition(c);
        }
        currentConditions.logic.put(currentCondition, Logic.OR);
        lastElement = BuilderElement.IS; // essentially we're a a special case of or().is(condition).
        return this;
    }

    public WhenBuilder or() {
        if (state == BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("You can not use or() after then()");
        switch(lastElement) {
            case A:
                //currentConditions.logic.put(currentThing, Logic.OR);
                // TODO find all illegal use for things
                break;
            case IS:
                currentConditions.logic.put(currentCondition, Logic.OR);
                break;
            default:
                throw new IllegalArgumentException("can only put or() after a(), an() is(), are() or a statement with a thing or condition as parameter");
        }

       lastElement = BuilderElement.OR;
       return this;
    }

    public WhenBuilder or(Object thing) { return or(thing, NAT); }
    public WhenBuilder or(Object thing, Object... moreThings) {
        or();
        a(thing, moreThings);
        return this;
    }

    public WhenBuilder not(Condition condition) { return not(condition, NAC); }
    public WhenBuilder not(Condition condition, Condition... moreConditions) {
        if (state == BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("You can not use not() after then()");

        Logic l = Logic.OR_NOT;
        switch (lastElement) {
            case A:
            case AND:
                l = Logic.AND_NOT;
            case OR:
                addCondition(condition);
                for (Condition c: moreConditions)
                    addCondition(c);
                currentConditions.logic.put(currentCondition, l);
                break;
            default:
                throw new IllegalArgumentException("You can only use not() after an(), is(), or() or and() (with no parameters in the case of the last three), optionally followed by is() or are()");
        }
        lastElement = BuilderElement.NOT;
        return this;
    }

    public WhenBuilder not() {
        if (state == BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("You can not use not() after then()");

        switch(lastElement) {
            case AND:
                if (currentCondition != null)
                    currentConditions.logic.put(currentCondition, Logic.AND_NOT);

                break;
            case OR:
                if (currentCondition != null)
                    currentConditions.logic.put(currentCondition, Logic.OR_NOT);
                break;
            default:
                throw new IllegalArgumentException("Can only use an empty not() after and() or or()");

        }
        lastElement = BuilderElement.NOT;

        return this;

    }

    public WhenBuilder then() {
        nextActs();
        lastElement = BuilderElement.THEN;
        state = BuildingState.CONSEQUENCES;
        return this;
    }

    public WhenBuilder then(Object thing) { return then(thing, NAT); }
    public WhenBuilder then(Object thing, Object...  moreThings) {
        then();
        with(thing, moreThings);
        return this;
    }

    public WhenBuilder then(Object thing, Act act) { return then(thing, act, NAA); }
    public WhenBuilder then(Object thing, Act act, Act... moreActs) {
        then(thing);
        act(act, moreActs);
        return this;
    }

    public WhenBuilder then(Act act, Object thing) { return then(act, thing, NAT); }
    public WhenBuilder then(Act act, Object thing, Object...  moreThings) {
        then(thing, moreThings);
        act(act);
        return this;
    }

    public WhenBuilder then(Act act) { return then(act, NAA); }
    public WhenBuilder then(Act act, Act... moreActs) {
        then();
        act(act, moreActs);
        return this;
    }

    public WhenBuilder and(Act act) { return and(act, NAA); }
    public WhenBuilder and(Act act, Act... moreActs) {
        and();
        then(act, moreActs);
        return this;
    }

    public WhenBuilder act(Act act) { return act(act, NAA); }
    public WhenBuilder act(Act act, Act... moreActs) {
        if (state != BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("act() can only be used after then()");

        addAct(act);
        for (Act a: moreActs)
            addAct(a);
        lastElement = BuilderElement.ACT;
        return this;
    }

    public WhenBuilder act(Object thing, Act act) { return act(thing, act, NAA); }
    public WhenBuilder act(Object thing, Act act, Act... moreActs) {
        with(thing);
        act(act, moreActs);
        return this;
    }

    public WhenBuilder with() {
        // allows for with().a(), with.an(), with().the() etc
        lastElement = BuilderElement.WITH;
        return this;
    }

    public WhenBuilder withResults(Act act, Act... moreActs) {
        act(act, moreActs);
        return with(false, false, true);
    }

    public WhenBuilder withResults() { return withThings(false, false, true, null); }
    public WhenBuilder with(Object thing) { return with(thing, NAT); }
    public WhenBuilder with(Object thing, Object...  moreThings) { return withThings(true, true, true, thing, moreThings); }
    public WhenBuilder withOnly(Object thing) { return withOnly(thing, NAT); }
    public WhenBuilder withOnly(Object thing, Object... moreThings) { return withThings(false, false, true, thing, moreThings); }
    public WhenBuilder withThings(boolean withThings, boolean withConditions, boolean withResults, Object thing, Object... moreThings) {
        if (state != BuildingState.CONSEQUENCES)
            throw new IllegalArgumentException("can only use with() after then()");

        currentActs.useConditionalThingsAsThings = withThings;
        currentActs.useConditionsAsThings = withConditions;
        currentActs.useResultsAsThings = withResults;

        if (thing != null)
            addThingForAct(thing);
        for (Object t: moreThings)
            addThingForAct(t);
        lastElement = BuilderElement.WITH;
        return this;
    }


    public boolean isWorking() {
        return working;
    }

    public WhenBuilder work() {
        working = true;
        if (evaluateConditions()) {
            doActs();
            for (PushConditionListener t: conditionListeners)
                t.push(this, true, true);
        }
        return this;
    }

    List<PushConditionListener> conditionListeners = new ArrayList<PushConditionListener>(0);

    @Override
    public void addListener(PushConditionListener listener, Object thing) {
        conditionListeners.add(listener);
    }

    @Override
    public void removeListener(PushConditionListener listener, Object thing) {
        conditionListeners.remove(listener);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (GroupOfConditionalThings conditions: conditionsGroups) {
            ret.append("when ");
            ret.append(conditions.toString());
        }

        for (GroupOfActsForThings acts: actsGroups) {
            ret.append("then ");
            ret.append(acts.toString());
        }
        return ret.toString().trim();

    }

}