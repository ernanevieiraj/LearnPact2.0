package br.ce.wcaquino.taskbackend.repo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.ce.wcaquino.taskbackend.model.Task;

public class TaskRepo {
	private Map<Long, Task> memory;
	private Long next;
	
	public TaskRepo() {
		memory = new HashMap<Long, Task>();
		memory.put(1L, new Task(1L, "Task Demo", LocalDate.now()));
		next = 2L;
	}

	public Task save(Task task) {
		Long id = getNext();
		Task toSave = new Task(id, task.getTask(), task.getDueDate());
		memory.put(id, toSave);
		return toSave.clone();
	}
	
	public Task update(Task task) {
		memory.put(task.getId(), task);
		return task.clone();
	}
	
	public Task deleteById(Long id) {
		return memory.remove(id);
	}
	
	public Task get(Long id) {
		return memory.get(id).clone();
	}
	
	public List<Task> findAll() {
		return memory.values().stream()
				.map(task -> task.clone()).collect(Collectors.toList());
	}
	
	private synchronized Long getNext() {
		return next++;
	}
}
