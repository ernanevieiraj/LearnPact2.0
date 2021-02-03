package pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasksfrontend.model.Todo;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.is;

public class GetTaskByIdTest {
    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact (PactDslWithProvider builder){

        DslPart body = new PactDslJsonBody()
                .numberType("id", 1L)
                .stringType("task", "Remember the Milk")
                .date("dueDate", "yyyy-MM-dd", new Date());

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
        TasksRepository consumer = new TasksRepository(mockProvider.getUrl());
        //Act
        Todo task = consumer.getTodo(1L);
        //Assert
        Assert.assertThat(task.getId(), CoreMatchers.is(1L));
        Assert.assertThat(task.getTask(), CoreMatchers.is("Remember the Milk"));
        Assert.assertThat(task.getDueDate(), is(LocalDate.now()));
    }
}
