package com.adityamhatre.knobapi;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application implements ApplicationRunner {
    static List<Integer> controlPins = new ArrayList<Integer>(4);


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        controlPins.add(7);
        controlPins.add(0);
        controlPins.add(2);
        controlPins.add(3);

        controlPins.forEach(pin -> GPIOProvider.Companion.getInstance().setPinToOutput(pin));
    }
}
