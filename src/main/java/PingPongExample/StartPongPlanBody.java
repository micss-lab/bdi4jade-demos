package PingPongExample;

import bdi4jade.plan.Plan;
import bdi4jade.plan.planbody.AbstractPlanBody;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class StartPongPlanBody extends AbstractPlanBody {
    private Logger log = Logger.getMyLogger(getClass().getName());
    public static final String MSG_CONTENT = "pong";

    int maxPongTimes=3;
    private int counter;
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null){
            ACLMessage reply = msg.createReply();
            if(msg.getPerformative()== ACLMessage.REQUEST){
                String content = msg.getContent();
                if ((content != null) && (content.indexOf("ping") != -1)){
                    log.info("Content: " + msg.getContent());
                    log.log(Logger.INFO, "Agent "+myAgent.getLocalName()+" - Received PING Request from "+msg.getSender().getLocalName());
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(MSG_CONTENT);
                    getBeliefBase().updateBelief(this.myAgent.getLocalName(), counter++);
                    log.info("Updating the Belief of "+myAgent.getLocalName()+"  "+getBeliefBase().getBeliefs());
                }
                else{
                    log.log(Logger.INFO, "Agent "+myAgent.getLocalName()+" - Unexpected request ["+content+"] received from "+msg.getSender().getLocalName());
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("( UnexpectedContent ("+content+"))");
                }

            }
            else {
                log.log(Logger.INFO, "Agent "+myAgent.getLocalName()+" - Unexpected message ["+ACLMessage.getPerformative(msg.getPerformative())
                        +"] received from "+msg.getSender().getLocalName());
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+") )");
                setEndState(Plan.EndState.FAILED);

            }
            myAgent.send(reply);
        }
        else if ((int) getBeliefBase().getBelief(this.myAgent.getLocalName()).getValue()==maxPongTimes)
        {
            setEndState(Plan.EndState.SUCCESSFUL);
        }
        else {
            block();
        }
    }
}