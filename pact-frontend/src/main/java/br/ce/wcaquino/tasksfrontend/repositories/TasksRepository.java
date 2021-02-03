package br.ce.wcaquino.tasksfrontend.repositories;

import br.ce.wcaquino.tasksfrontend.model.Todo;
import org.springframework.web.client.RestTemplate;


public class TasksRepository {
    private String url;

    public TasksRepository(String url){
        this.url = url;
    }

    public Todo[] getTodos() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                url + "/todo", Todo[].class);
    }

    public Todo save(Todo todo) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(
                url + "/todo", todo,  Todo.class);
    }

    public void update(Todo todo) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(
                url + "/todo/" + todo.getId(), todo);
    }

    public void delete(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(
                url + "/todo/" + id);
    }

    public Todo getTodo(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                url + "/todo/" + id, Todo.class);
    }
}
