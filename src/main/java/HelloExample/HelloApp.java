package HelloExample;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.io.IOException;


public class HelloApp {

    public static void main(String[] args) throws IOException {
        try {
            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            properties.setProperty(Profile.MAIN, "true");
            properties.setProperty(Profile.CONTAINER_NAME, "My-Container");

            Profile profile = new ProfileImpl(properties);
            profile.setParameter(profile.MAIN_HOST, "localhost");

            AgentContainer agentContainer = runtime.createMainContainer(profile);

            AgentController agent = agentContainer.createNewAgent("HelloAgent",
                    "HelloExample.HelloAgent", new Object[]{});
            agent.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}