package happy.rabbit;

import happy.rabbit.jenkins.NetworkServiceImpl;

public class Main {

    public static void main(String[] args) {
        System.out.println(new NetworkServiceImpl().fillJobNameAndDescription(68, "REG", "From apache http"));
    }
}
