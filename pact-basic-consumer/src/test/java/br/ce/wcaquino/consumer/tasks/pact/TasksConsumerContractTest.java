package br.ce.wcaquino.consumer.tasks.pact;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.tasks.service.TasksConsumer;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class TasksConsumerContractTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "BasicConsumer")
    public RequestResponsePact createPact (PactDslWithProvider builder){

        DslPart body = new PactDslJsonBody()
                .numberType("id", 1L)
                .stringType("task")
                .stringType("dueDate");

        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Retrieve Task #1")
                    .path("/todo/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(body)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        //Arranje
        TasksConsumer consumer = new TasksConsumer(mockProvider.getUrl());
        //Act
        Task task = consumer.getTask(1L);
        //Assert
        Assert.assertThat(task.getId(), CoreMatchers.is(1L));
        Assert.assertThat(task.getTask(), CoreMatchers.is(CoreMatchers.notNullValue()));
    }
}

