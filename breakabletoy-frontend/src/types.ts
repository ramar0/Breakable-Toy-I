export interface Task {
    id: number;
    title: string;
    desc: string;
    priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'ERROR';
    deadline: string;
    finDate: string;
    fin: boolean;
}