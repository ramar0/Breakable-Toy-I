import React, {useEffect, useState} from 'react';
import {Task} from './types';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import logo from './logo.svg';
import './App.css';

const App: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [filterStatus, setFilterStatus] = useState('ALL');
  const [filterPriority, setFilterPriority] = useState('ALL');
  const [filterTask, setFilterTask] = useState('');
  const [sortBy, setSort] = useState('deadline');
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalAvg, setAvg] = useState(0);
  const [highAvg, setHighAvg] = useState(0);
  const [mediumAvg, setMediumAvg] = useState(0);
  const [lowAvg, setLowAvg] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

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

  const fetchTasks = async () => {
    try{
      const res = await fetch(`http://127.0.0.1:9090/api/tasks?status=${filterStatus}&priority=${filterPriority}&search=${filterTask}&sortBy=${sortBy}&page=${page}&size=${size}`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });

      if (res.ok){
        const data = await res.json();
        setTasks(data.content);
        setTotalPages(data.totalPages);
        setAvg(data.totalAvg);
        setHighAvg(data.highAvg);
        setMediumAvg(data.mediumAvg);
        setLowAvg(data.lowAvg);
      } else {
        console.error('Error Fetching Tasks');
      }
    } catch (error) {
      console.error('Error during petition: ', error);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [filterStatus, filterPriority, filterTask, sortBy, page]);

  const addOrUpdateTask = async (task: Omit<Task, 'id'>, id?: number) => {
    if (id) {
      //await axios.put(`http://127.0.0.1:9090/api/tasks?id=${id}`, task);

      const formBody = new URLSearchParams({
        id: id.toString(),
        title: task.title,
        desc: task.desc,
        prio: prioritySwitch(task.priority),
        deadL: task.deadline,
        fin: task.fin.toString(),
        finDate: task.finDate,
      })

      try{
        const response = await fetch("http://127.0.0.1:9090/api/tasks", {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: formBody.toString(),
        });

        if (response.ok){
          console.log('Task Updated Succesfully');
        } else {
          console.error('Error Updating Task');
        }
      } catch (error) {
        console.error('Error during petition: ', error);
      }
    } else {
      //await axios.post(`http://127.0.0.1:9090/api/tasks?${id}`, task);

      const formBody = new URLSearchParams({
        title: task.title,
        desc: task.desc,
        prio: prioritySwitch(task.priority),
        deadL: task.deadline,
      })

      try{
        const response = await fetch("http://127.0.0.1:9090/api/tasks", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: formBody.toString(),
        });

        if (response.ok){
          console.log('Task Added Succesfully');
        } else {
          console.error('Error Adding Task');
        }
      } catch (error) {
        console.error('Error during petition: ', error);
      }
    }
    fetchTasks();
    setEditingTask(null);
  };

  const deleteTask = async (id: number) => {
    //await axios.delete(`http://127.0.0.1:9090/api/tasks/${id}`);

    const formBody = new URLSearchParams({
      id: id.toString(),
    })

    try{
      const response = await fetch("http://127.0.0.1:9090/api/tasks", {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formBody.toString(),
      });

      if (response.ok){
        console.log('Task Deleted Succesfully');
      } else {
        console.error('Error Deleting Task');
      }
    } catch (error) {
      console.error('Error during petition: ', error);
    }
    
    fetchTasks();
  };

  const toggleFinished = async (id: number) => {
    const task = tasks.find(t => t.id === id);
    if (!task) return;
    //await axios.put(`http://127.0.0.1:9090/api/tasks/${id}`, {
    //  ...task,
    //  fin: !task.fin,
    //});

    const formBody = new URLSearchParams({
      id: id.toString(),
      title: task.title,
      desc: task.desc,
      prio: prioritySwitch(task.priority),
      deadL: task.deadline,
      fin: true.toString(),
      finDate: new Date().toISOString().split('T')[0],
    })
      
    try{
      const response = await fetch("http://127.0.0.1:9090/api/tasks", {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formBody.toString(),
      });

      if (response.ok){
        console.log('Task Finished Succesfully');
      } else {
        console.error('Error Updating Task');
      }
    } catch (error) {
      console.error('Error during petition: ', error);
    }

    fetchTasks();
  };

  const handlePages = (dir: string) => {
    if(dir === '<'){
      setPage(page - 1);
    } else if (dir === '>'){
      setPage(page + 1);
    }
  };

  return (
    <div className='App'>
      <h1>Task Inventory</h1>
      <TaskForm onSubmit={addOrUpdateTask} editingTask={editingTask} />
      <div className='filters'>
        <input
          placeholder="Task"
          value={filterTask}
          onChange={e => setFilterTask(e.target.value)}
        />
        <select value={filterPriority} onChange={e => setFilterPriority(e.target.value)}>
          <option value="ALL">ALL</option>
          <option value="HIGH">HIGH</option>
          <option value="MEDIUM">MEDIUM</option>
          <option value="LOW">LOW</option>
        </select>
        <select value={sortBy} onChange={e => setSort(e.target.value)}>
          <option value="deadline">DEADLINE</option>
          <option value="priority">PRIORITY</option>
        </select>
        <select value={filterStatus} onChange={e => setFilterStatus(e.target.value)}>
          <option value="ALL">ALL</option>
          <option value="FINISHED">FINISHED</option>
          <option value='UNFINISHED'>UNFINISHED</option>
        </select>
      </div>
      <TaskList
        tasks={tasks}
        onDelete={deleteTask}
        onToggleFinish={toggleFinished}
        onEdit={setEditingTask}
        filterStatus={filterStatus}
        filterPriority={filterPriority}
        filterTask={filterTask}
        sortBy={sortBy}
      />
      <div className='Page'>
        <button disabled={page === 0} onClick={() => setPage(page - 1)}> {'<'} </button>
        <span>Page {page + 1} of {totalPages}</span>
        <button disabled={(page+1) >= totalPages} onClick={() => setPage(page + 1)}> {'>'} </button>
      </div>
      <div className='Avg'>
        <div><span>Average time in munutes to finish tasks: {totalAvg}</span></div>
        <div>
          <span>Average time in minutes to finish tasks by priority:</span>
          <br></br>
          <span>LOW: {lowAvg}</span>
          <br></br>
          <span>MEDIUM: {mediumAvg}</span>
          <br></br>
          <span>HIGH: {highAvg}</span>
        </div>
      </div>
    </div>
  );
};

export default App;
