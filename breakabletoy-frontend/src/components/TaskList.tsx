import React from 'react';
import {Task} from '../types';

interface TaskListProps {
    tasks: Task[];
    onDelete: (id:number) => void;
    onToggleFinish: (id:number) => void;
    onEdit: (task: Task) => void;
    filterStatus: string,
    filterPriority: string;
    filterTask: string;
    sortBy: string;
}

const TaskList: React.FC<TaskListProps> = ({tasks,
                                            onDelete,
                                            onToggleFinish,
                                            onEdit,
                                            filterStatus,
                                            filterPriority,
                                            filterTask,
                                            sortBy
                                            }) => {

    const prioritySwitch = (prio: any ) => {
        if (!isNaN(prio)){
            switch(prio){
                case 0:
                    return "LOW";
                case 1:
                    return "MEDIUM";
                case 2:
                    return "HIGH";
                default:
                    return "ERROR";
            }
        } else {
            return prio;
        }
    };

    const filteredTasks = tasks
        .filter(task =>
            filterStatus === 'ALL' || task.fin === (filterStatus === "FINISHED") || task.fin === !(filterStatus === "UNFINISHED")
        )
        .filter(task =>
            filterPriority === 'ALL' || prioritySwitch(task.priority) === filterPriority
        )
        .filter(task =>
            task.title.toString().toLowerCase().includes(filterTask.toLowerCase())
        )
        .sort((a, b) => {
            if (sortBy === 'deadline') {
                return (new Date(a.deadline).getTime() - new Date(b.deadline).getTime());
            } else if (sortBy === 'priority'){
                a.priority = prioritySwitch(a.priority);
                b.priority = prioritySwitch(b.priority);
                const priorityOrder = { ERROR: 3, HIGH: 2, MEDIUM: 1, LOW:0};
                return  (priorityOrder[b.priority]) - (priorityOrder[a.priority]);
            }
            return 0;
        });

    return (
        <ul>
            {filteredTasks.map(task => (
            <li key={task.id} className={`task ${task.fin ? 'finished' : ''}`}>
                <span onClick={() => onToggleFinish(task.id)}>
                    {task.title} | {task.desc} | {task.deadline} | {prioritySwitch(task.priority)}
                </span>
                <button onClick={() => onEdit(task)}> EDIT </button>
                <button onClick={() => onDelete(task.id)}> DELETE </button>
            </li>
            ))}
        </ul>
    );
};

export default TaskList;