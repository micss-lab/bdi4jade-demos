package PingPongExample;

import bdi4jade.plan.Plan;
import bdi4jade.plan.planbody.AbstractPlanBody;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import java.sql.Timestamp;

public class StartPingPlanBody extends AbstractPlanBody {
    private Logger log = Logger.getMyLogger(getClass().getName());
    public static final String MSG_CONTENT = "ping";

    int maxPingTimes=7;
    private int counter;
    private MessageTemplate mt;
    public static volatile boolean msgSent;
    public synchronized void action() {
        if (msgSent==false && this.myAgent.getLocalName().equals(PingPongApp.AGENT_1)) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(MSG_CONTENT);
            msg.addReceiver(new AID(PingPongApp.AGENT_2, AID.ISLOCALNAME));
            msg.setConversationId("Ping " + new Timestamp(System.currentTimeMillis()));
            msg.setReplyWith("Inform " + new Timestamp(System.currentTimeMillis()));
            this.myAgent.send(msg);
            msgSent = true;
            this.mt = MessageTemplate.and(MessageTemplate
                            .MatchConversationId(msg.getConversationId()),
                    MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
            log.log(Logger.INFO, "Agent "+myAgent.getLocalName()+" - Sent PING Request");
        }
        else if (msgSent==false && this.myAgent.getLocalName().equals(PingPongApp.AGENT_2)) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(MSG_CONTENT);
            msg.addReceiver(new AID(PingPongApp.AGENT_1, AID.ISLOCALNAME));
            msg.setConversationId("Ping " + new Timestamp(System.currentTimeMillis()));
            msg.setReplyWith("Inform " + new Timestamp(System.currentTimeMillis()));
            this.myAgent.send(msg);
            msgSent = true;
            this.mt = MessageTemplate.and(MessageTemplate
                            .MatchConversationId(msg.getConversationId()),
                    MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
            log.log(Logger.INFO, "Agent "+myAgent.getLocalName()+" - Sent PING Request");

        }
        else {
            ACLMessage reply = myAgent.receive(mt);
            if (reply != null) {
                log.log(Logger.INFO, "Agent "+myAgent.getLocalName()+" - Received PONG action from "+reply.getSender().getLocalName());
                log.info("Content: " + reply.getContent()+" Counter: "+counter);
                getBeliefBase().updateBelief(this.myAgent.getLocalName(), counter++);
                log.info("Updating the Belief of "+myAgent.getLocalName()+"  "+getBeliefBase().getBeliefs());
                if ( (int) getBeliefBase().getBelief(this.myAgent.getLocalName()).getValue() == maxPingTimes) {
                    setEndState(Plan.EndState.SUCCESSFUL);
                } else {
                    this.msgSent = false;
                }
            } else {
                block();
            }
        }
    }
}