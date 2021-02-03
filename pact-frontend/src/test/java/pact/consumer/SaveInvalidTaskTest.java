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

public class SaveInvalidTaskTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact (PactDslWithProvider builder){

        DslPart requestBody = new PactDslJsonBody()
                .nullValue("id")
                .nullValue("task")
                .nullValue("dueDate");

        DslPart responseBody = new PactDslJsonBody()
                .stringType("message", "Fill the task description");


        return builder
                .uponReceiving("Save a Invalid Task")
                .path("/todo")
                .method("POST")
                .matchHeader("Content-type", "application/json.*", "application/json")
                .body(requestBody)
                .willRespondWith()
                .status(400)
                .body(responseBody)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        //Arrange
        TasksRepository consumer = new TasksRepository(mockProvider.getUrl());
        //Act
        try {
            consumer.save(new Todo(null, null, null));
            Assert.fail("Should throws an exception");
        }catch(Exception ex){
          System.out.println(ex.getMessage());
            Assert.assertThat(ex.getMessage(), CoreMatchers.containsString("400 Bad Request"));
            Assert.assertThat(ex.getMessage(), CoreMatchers.containsString("Fill the task description"));
        }
    }
}
