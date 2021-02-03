package br.ce.wcaquino.tasksfrontend.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ce.wcaquino.tasksfrontend.repositories.TasksRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import br.ce.wcaquino.tasksfrontend.model.Todo;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRepository;


@Controller
public class TasksController {
	
	@Value("${backend.host}")
	private String BACKEND_HOST;

	@Value("${backend.port}")
	private String BACKEND_PORT;
	
	public String getBackendURL() {
		return "http://" + BACKEND_HOST + ":" + BACKEND_PORT;
	}

	private TasksRepository tasksRepo;

	public TasksRepository getTasksRepo(){
		if (tasksRepo == null){
			tasksRepo = new TasksRepository("http://" + BACKEND_HOST + ":" + BACKEND_PORT);
		}
		return tasksRepo;
	}

	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("todos", getTasksRepo().getTodos());
		return "index";
	}
	
	@GetMapping("add")
	public String add(Model model) {
		model.addAttribute("todo", new Todo());
		return "add";
	}

	@PostMapping("save")
	public String save(Todo todo, Model model) {
		try {
			if(todo.getId() == null) {
				getTasksRepo().save(todo);
			}
			else {
				getTasksRepo().update(todo);
			}
			model.addAttribute("sucess", "Sucess!");
			return "index";
		} catch(Exception e) {
			Pattern compile = Pattern.compile("message\":\"(.*)\",");
			Matcher m = compile.matcher(e.getMessage());
			m.find();
			model.addAttribute("error", m.group(1));
			model.addAttribute("todo", todo);
			return "add";
		} finally {
			model.addAttribute("todos", getTasksRepo().getTodos());
		}
	}


	@GetMapping("delete/{id}")
	public String delete(@PathVariable Long id, Model model) {
		getTasksRepo().delete(id);
		model.addAttribute("success", "Success!");
		model.addAttribute("todos", getTasksRepo().getTodos());
		return "index";
	}
	
	@GetMapping("edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		Todo todo = getTasksRepo().getTodo(id);
		if(todo == null) {
			model.addAttribute("error", "Invalid Task");
			model.addAttribute("todos", getTasksRepo().getTodos());
			return "index";
		}
		model.addAttribute("todo", todo);
		return "add";
	}
	


}
