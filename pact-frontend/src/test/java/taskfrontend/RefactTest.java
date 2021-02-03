package taskfrontend;

import br.ce.wcaquino.tasksfrontend.controller.TasksController;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRepository;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.lang.reflect.Field;

public class RefactTest {

    @Test
    public void before() throws NoSuchFieldException, IllegalAccessException {
        TasksController service = new TasksController();
        Field host = service.getClass().getDeclaredField("BACKEND_HOST");
        host.setAccessible(true);
        host.set(service, "localhost");
        Field port = service.getClass().getDeclaredField("BACKEND_PORT");
        port.setAccessible(true);
        port.set(service, "8000");
        Model model = new RedirectAttributesModelMap();
        service.index(model);
        System.out.print(model.getAttribute("todos"));

    }

    @Test
    public void after(){
        TasksRepository repo = new TasksRepository("http://localhost:8000");
        System.out.println(repo.getTodos()[0]);
    }
}
