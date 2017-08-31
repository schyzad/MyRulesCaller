package com.soprasteria.caller;

import com.soprasteria.bpm.rules.myrulesproject.Person;

import org.kie.api.command.BatchExecutionCommand;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.kie.api.KieServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.core.command.impl.GenericCommand;


public class MyAppTest {


    Person p;

    public static void main(String[] args){

        Person p1 = new Person();
        p1.setFirstName("Anton");
        p1.setLastName("RedHat 1");
        p1.setHourlyRate(11);
        p1.setWage(20);


        String url = "http://localhost:8080/kie-server/services/rest/server";
        String username = "kieserver";
        String password = "kieserver1!";
        String container = "myContainers";
        String session = "myStatelessSession";

        KieServicesConfiguration config = KieServicesFactory
                .newRestConfiguration(url, username, password);
        Set<Class<?>> allClasses = new HashSet<Class<?>>();
        allClasses.add(Person.class);
        config.addExtraClasses(allClasses);
        System.out.println("config added.");
        KieServicesClient client  = KieServicesFactory.newKieServicesClient(config);
        RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
        List<GenericCommand<?>> commands = new ArrayList<GenericCommand<?>>();

        commands.add((GenericCommand<?>) KieServices.Factory
                .get().getCommands().newInsert(p1,"Person Insert IDsss"));
        commands.add((GenericCommand<?>) KieServices.Factory
                .get().getCommands().newFireAllRules("fire-identifier"));

        BatchExecutionCommand batchCommand = KieServices.Factory
                .get().getCommands().newBatchExecution(commands,session);
        System.out.println("running rules.");
        ServiceResponse<String> response = ruleClient.executeCommands(container, batchCommand);

        System.out.println("printing result. "+response.getType().toString());
        System.out.println(response.getResult());

    }

}
