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
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;

public class UpdateTaskTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart requestBody = new PactDslJsonBody()
                .numberType("id", 1)
                .stringType("task", "Task Updated")
                .array("dueDate").numberType(2021).numberType(02).numberType(03).closeArray();

        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Update a Task")
                .path("/todo/1")
                .method("PUT")
                .matchHeader("Content-type", "application/json.*", "application/json")
                .body(requestBody)
                .willRespondWith()
                .status(200)
                .toPact();
    }
        @Test
        @PactVerification
        public void shouldUpdateTask(){

            TasksRepository repo = new TasksRepository(mockProvider.getUrl());
            repo.update(new Todo(1L, "Task Updated", LocalDate.now()));

        }


}
