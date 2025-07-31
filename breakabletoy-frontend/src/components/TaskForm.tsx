import React, {useState, useEffect} from 'react';
import {Task} from '../types';

interface TaskFormProps {
    onSubmit: (task: Omit<Task, 'id'>, id?: number) => void;
    editingTask?: Task | null;
}

const TaskForm: React.FC<TaskFormProps> = ({ onSubmit, editingTask}) => {
    const [title, setTitle] = useState('');
    const [desc, setDesc] = useState('');
    const [deadline, setDeadLine] = useState('');
    const [priority, setPriority] = useState<'LOW' | 'MEDIUM' | 'HIGH' | 'ERROR'>('LOW');
    const [finDate, setFinDate] = useState('');
    const [fin, setFin] = useState(false);

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
    }

    useEffect(() => {
        if (editingTask) {
            setTitle(editingTask.title);
            setDesc(editingTask.desc);
            setPriority(prioritySwitch(editingTask.priority));
            setDeadLine(editingTask.deadline);
            setFinDate(editingTask.finDate);
        }
    }, [editingTask]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit({title, desc, priority, deadline, finDate, fin}, editingTask?.id);
        setTitle('');
        setDesc('');
        setDeadLine('');
        setPriority('LOW')
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                required
                placeholder='Title'
                value={title}
                onChange={e => setTitle(e.target.value)}
            />
            <input
                required
                placeholder='Description'
                value={desc}
                onChange={e => setDesc(e.target.value)}
            />
            <input
                placeholder='YYYY-mm-dd'
                value={deadline}
                onChange={e => setDeadLine(e.target.value)}
            />
            <select value={priority}
                    onChange={e => setPriority(e.target.value as any)}
            >
                <option value="HIGH">HIGH</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="LOW">LOW</option>
            </select>
            <button type='submit'>{editingTask ? 'Update' : 'Add Task'}</button>
        </form>
    );
};

export default TaskForm;