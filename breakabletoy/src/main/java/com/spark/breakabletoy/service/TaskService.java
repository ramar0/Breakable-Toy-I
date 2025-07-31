package com.spark.breakabletoy.service;

import com.spark.breakabletoy.model.Task;
import com.spark.breakabletoy.model.TaskPage;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public List<Task> getAllTasks(String status, String priority, String search, String sortBy){
        return tasks.stream()
            .filter(task -> {
                if (status != null){
                    boolean fin = status.equalsIgnoreCase("Finished");
                    if (task.getFin() != fin) return false;
                }
                return true;
            })
            .filter(task -> {
                if (priority != null) {
                    String frontPrio;

                    switch (task.getPriority()) {
                        case 0:
                            frontPrio = "LOW";
                            break;
                        case 1:
                            frontPrio = "MEDIUM";
                            break;
                        case 2:
                            frontPrio = "HIGH";
                            break;
                        default:
                            frontPrio = "ERROR";
                            break;
                    }
                    return frontPrio.equalsIgnoreCase(priority);
                }
                return true;
            })
            .filter(task -> {
                if (search != null && !search.isEmpty()){
                    return task.getTitle().toLowerCase().contains(search.toLowerCase());
                }
                return true;
            })
            .sorted((a, b) -> {
                if ("dueDate".equalsIgnoreCase(sortBy)){
                    return a.getDeadline().compareTo(b.getDeadline());
                } else if ("Priority".equalsIgnoreCase(sortBy)){
                    return (""+b.getPriority()).compareTo(""+a.getPriority());
                } else {
                    return 0;
                }
            })
            .collect(Collectors.toList());
    }

    public TaskPage<Task> getTaskPage(String status, String priority, String search, String sortBy, int page, int size){
        List<Task> allTasks = tasks.stream()
            .filter(task -> {
                if (status != null){
                    boolean fin = status.equalsIgnoreCase("Finished");
                    if (task.getFin() != fin) return false;
                }
                return true;
            })
            .filter(task -> {
                if (priority != null) {
                    String frontPrio;

                    switch (task.getPriority()) {
                        case 0:
                            frontPrio = "LOW";
                            break;
                        case 1:
                            frontPrio = "MEDIUM";
                            break;
                        case 2:
                            frontPrio = "HIGH";
                            break;
                        default:
                            frontPrio = "ERROR";
                            break;
                    }
                    return frontPrio.equalsIgnoreCase(priority);
                }
                return true;
            })
            .filter(task -> {
                if (search != null && !search.isEmpty()){
                    return task.getTitle().toLowerCase().contains(search.toLowerCase());
                }
                return true;
            })
            .sorted((a, b) -> {
                if ("dueDate".equalsIgnoreCase(sortBy)){
                    return a.getDeadline().compareTo(b.getDeadline());
                } else if ("Priority".equalsIgnoreCase(sortBy)){
                    return (""+b.getPriority()).compareTo(""+a.getPriority());
                } else {
                    return 0;
                }
            })
            .collect(Collectors.toList());

        int totalElements = allTasks.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        long totalMinutes = 0;
        int totalCompleted = 0;

        Map<String, long[]> stats = new HashMap<>();

        for (Task task: tasks){
            if(task.getFin()){
                Duration duration = Duration.between(OffsetDateTime.parse(task.getCreation()), OffsetDateTime.parse(task.getFinDate()));
                long minutes = duration.toMinutes();
                totalMinutes += minutes;
                totalCompleted++;

                String strPrio;

                switch(task.getPriority()){
                    case 0:
                        strPrio = "LOW";
                        break;
                    case 1:
                        strPrio = "MEDIUM";
                        break;
                    case 2:
                        strPrio = "HIGH";
                        break;
                    default:
                        strPrio = "ERROR";
                        break;
                }

                stats.putIfAbsent(strPrio, new long[2]);
                stats.get(strPrio)[0] += minutes;
                stats.get(strPrio)[1] += 1;
            }
        }

        long totalAvgMinutes = totalCompleted > 0 ? totalMinutes / totalCompleted : 0;
        long avgMinutesLow = stats.containsKey("LOW") && stats.get("LOW")[1] > 0 ? stats.get("LOW")[0] / stats.get("LOW")[1] : 0;
        long avgMinutesMedium = stats.containsKey("MEDIUM") && stats.get("MEDIUM")[1] > 0 ? stats.get("MEDIUM")[0] / stats.get("MEDIUM")[1] : 0;
        long avgMinutesHigh = stats.containsKey("HIGH") && stats.get("HIGH")[1] > 0 ? stats.get("HIGH")[0] / stats.get("HIGH")[1] : 0;

        List<Task> paginatedTasks = (fromIndex > totalElements) ? Collections.emptyList() : allTasks.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) totalElements/size);

        return new TaskPage<>(paginatedTasks, page, size, totalPages, totalElements, totalAvgMinutes, avgMinutesHigh, avgMinutesMedium, avgMinutesLow);
    }

    public Task getTaskByID(int id){
        return tasks.stream()
            .filter(task -> (task.getID() == id))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Task not Found"));
    }

    public Task createTask(Task task){
        task.setID(nextId);
        tasks.add(task);
        nextId++;
        return task;
    }

    public Task updateTask (int id, Task task){
        Task updTask = getTaskByID(id);

        updTask.setTitle(task.getTitle());
        updTask.setDesc(task.getDesc());
        updTask.setPriority(task.getPriority());
        updTask.setDeadline(task.getDeadline());
        if (!task.getFin()){
            updTask.setFinDate(null);
        } else if (!updTask.getFin() && (updTask.getFinDate() == null || updTask.getFinDate().isEmpty())) {
            updTask.setFinDate(task.getFinDate());
        }
        updTask.setFin(task.getFin());

        return updTask;
    }

    public void deleteTask (int id){
        Task delTask = getTaskByID(id);
        tasks.remove(delTask);
    }
}