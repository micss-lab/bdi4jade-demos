package PingPongExample;

import bdi4jade.annotation.Belief;
import bdi4jade.annotation.Plan;
import bdi4jade.belief.TransientBelief;
import bdi4jade.core.SingleCapabilityAgent;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.DefaultPlan;
import jade.util.Logger;
import java.util.Scanner;


public class PingCapability extends SingleCapabilityAgent {
    private Logger log = Logger.getMyLogger(getClass().getName());
    @Plan
    private static bdi4jade.plan.Plan Plan1 = new DefaultPlan(PingPongGoal.class, StartPingPlanBody.class);
    @Plan
    private static bdi4jade.plan.Plan Plan2 = new DefaultPlan(PingPongGoal.class, StartPongPlanBody.class);
    @Belief
    static bdi4jade.belief.TransientBelief<String, Integer> agentAndPingTimes=new TransientBelief<>();

    public static class PingPongGoal implements Goal {
        PingPongGoal(String string, int pingTimes) {
            agentAndPingTimes.setName(string);
            agentAndPingTimes.setValue(pingTimes);
        }
    }

    public void init() {

        Scanner s = new Scanner(System.in);
        System.out.println("Initialize Ping Capability (Y/N): ");
        String s1 = s.nextLine();
        if (s1.startsWith("y") || s1.startsWith("Y")) {
            System.out.println("PING Capability Initialized");
            addGoal(new PingPongGoal(this.getLocalName(), 0), new PingPongGoalListener());
            getCapability().getBeliefBase().addBelief(agentAndPingTimes);
            getCapability().getPlanLibrary().addPlan(Plan1);


        }
    }

    public class PingPongGoalListener implements GoalListener {
        private Logger log = Logger.getMyLogger(getClass().getName());

        @Override
        public void goalPerformed(GoalEvent goalEvent) {
            if (goalEvent.getStatus() == GoalStatus.ACHIEVED) {
                log.info("Agent " + getCapability().getMyAgent().getLocalName()
                        + " Goal Status: " + goalEvent.getStatus());
            } else if (goalEvent.getStatus() == GoalStatus.PLAN_FAILED) {
                log.info("Agent " + getCapability().getMyAgent().getLocalName()
                        + " Goal Status: " + goalEvent.getStatus());
            }
        }
    }
}
