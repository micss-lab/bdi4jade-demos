package HelloExample;

import bdi4jade.annotation.Belief;
import bdi4jade.belief.TransientBelief;
import bdi4jade.core.SingleCapabilityAgent;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

import java.util.Scanner;

public class HelloAgent extends SingleCapabilityAgent {
    @Belief
    static TransientBelief<String, String> agentIdentity = new TransientBelief<>();

    @bdi4jade.annotation.Plan
    private static bdi4jade.plan.Plan Plan1 = new DefaultPlan(HelloAgent.HelloGoal.class, HelloAgent.HelloPlanBody1.class);

    @bdi4jade.annotation.Plan
    private static bdi4jade.plan.Plan Plan2 = new DefaultPlan(HelloAgent.HelloGoal.class, HelloAgent.HelloPlanBody2.class);

    public HelloAgent() throws IllegalAccessException {
//        getCapability().getPlanLibrary()
//                .addPlan(new DefaultPlan(HelloAgent.HelloWorldGoal.class, HelloAgent.HelloWorldPlanBody.class));
//        getCapability().getPlanLibrary().addPlan(new Plan1());
        addGoal(new HelloGoal("James Bond", "007"), new HelloGoalListener());
//        getCapability().getPlanLibrary().addPlan(Plan1);
        getCapability().getPlanLibrary().addPlan(Plan2);
        getCapability().getBeliefBase().addBelief(agentIdentity);
    }

    //    Intention intention = new Intention(new HelloGoal("a","b"), this);
    public static class HelloGoal implements Goal {
        private String string;

        HelloGoal(String name, String code) {
            agentIdentity.setName(name);
            agentIdentity.setValue(code);
        }

        public String getName() {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Your Agent Name: ");
            string = s.nextLine();
            return string;
        }

        public String getCode() {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Your Agent Code: ");
            string = s.nextLine();
            return string;
        }
    }

    public static class HelloPlanBody1 extends AbstractPlanBody {
        public void action() {
            String s;
            s = ((HelloAgent.HelloGoal) getGoal()).getName();
//            System.out.println(this.getCapability().getBeliefBase().getBelief(agentIdentity.getName()).getName());
//            System.out.println(this.getCapability().getBeliefBase().getBelief(agentIdentity.getName()).getValue());

            if (s.equals(this.getCapability().getBeliefBase().getBelief(agentIdentity.getName()).getName())) {
                System.out.println("Hello, " + s + " Agent!, I'm " + getAgent().getLocalName());
                setEndState(EndState.SUCCESSFUL);
            } else {
                System.out.println("Your Identity is fake");
                setEndState(EndState.FAILED);
                getCapability().getPlanLibrary().addPlan(Plan2);
            }
        }
    }

    public static class HelloPlanBody2 extends AbstractPlanBody {
        public void action() {
            String s;
            s = ((HelloAgent.HelloGoal) getGoal()).getCode();
            if (s.equals(agentIdentity.getValue())) {
                System.out.println("Hello, " + s + " Agent!, I'm " + getAgent().getLocalName());
                setEndState(EndState.SUCCESSFUL);
            } else {
                System.out.println("Your Identity is fake");
                setEndState(EndState.FAILED);
                getCapability().getPlanLibrary().addPlan(Plan1);
            }
        }
    }

    public class HelloGoalListener implements GoalListener {
        @Override
        public void goalPerformed(GoalEvent goalEvent) {
            if (goalEvent.getGoal().getClass() == HelloAgent.HelloGoal.class &&
                    goalEvent.getStatus() == GoalStatus.ACHIEVED) {
                System.out.println("Goal Status: " + goalEvent.getStatus() + " For The " + getCapability().getMyAgent().getLocalName());
            } else if (goalEvent.getStatus() == GoalStatus.PLAN_FAILED) {
                System.out.println("Goal Status: " + goalEvent.getStatus() + " For The " + getCapability().getMyAgent().getLocalName());
            }
        }
    }
}
