package com.spark.breakabletoy;

import com.spark.breakabletoy.model.Task;
import com.spark.breakabletoy.model.TaskPage;
import com.spark.breakabletoy.service.TaskService;

import java.time.Instant;
import java.util.Date;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:8080")
public class TaskController {
    
    @Autowired
    private TaskService service;

    @GetMapping
    public TaskPage<Task> getTasks( @RequestParam(required = false) String status,
                                    @RequestParam(required = false) String priority,
                                    @RequestParam(required = false) String search,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {

        if(status != null && (status.isEmpty() || status.equals("ALL"))){
            status = null;
        }
        if(priority != null && (priority.isEmpty() || priority.equals("ALL"))){
            priority = null;
        }
        if(search != null && search.isEmpty()){
            search = null;
        }


        return service.getTaskPage(status, priority, search, sortBy, page, size);
        //return service.getAllTasks(status, priority, search, sortBy);
    }

    @PostMapping
    public Task create( @RequestParam(required = true) String title,
                        @RequestParam(required = true) String desc,
                        @RequestParam(required = true) String prio,
                        @RequestParam(required = true) String deadL) {

        int truPrio;
        
        switch(prio){
            case "LOW":
                truPrio = 0;
                break;
            case "MEDIUM":
                truPrio = 1;
                break;
            case "HIGH":
                truPrio = 2;
                break;
            default:
                truPrio = 3;
                break;
        }

        Task newTask = new Task(title, desc, truPrio, deadL);
        
        return service.createTask(newTask);
    }

    @PutMapping
    public Task update( @RequestParam int id,
                        @RequestParam(required = true) String title,
                        @RequestParam(required = true) String desc,
                        @RequestParam(required = true) String prio,
                        @RequestParam(required = true) String deadL,
                        @RequestParam(required = true) boolean fin) {

        int truPrio;
        
        switch(prio){
            case "LOW":
                truPrio = 0;
                break;
            case "MEDIUM":
                truPrio = 1;
                break;
            case "HIGH":
                truPrio = 2;
                break;
            default:
                truPrio = 3;
                break;
        }

        String finDate = "";

        if (fin){
            finDate = Instant.now().toString();
        }

        Task newTask = new Task(id, title, desc, truPrio, deadL, fin, finDate);

        return service.updateTask(id, newTask);
    }

    @DeleteMapping
    public void delete(@RequestParam int id) {
        service.deleteTask(id);
    }
}