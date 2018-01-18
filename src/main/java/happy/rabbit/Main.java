package happy.rabbit;

import happy.rabbit.jenkins.Network;

public class Main {

    public static void main(String[] args) {
        System.out.println(new Network()
                .fillJobNameAndDescription(54, "ENV", "Blabla"));
    }
}
