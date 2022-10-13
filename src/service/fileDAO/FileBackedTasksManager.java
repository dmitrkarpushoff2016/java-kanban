package service.fileDAO;

import model.*;
import Enum.*;
import service.history.HistoryManager;
import service.manager.InMemoryTaskManager;
import service.exception.ManagerSaveException;

import java.io.*;
import java.util.*;

/**
 * @author Дмитрий Карпушов 31.08.2022
 */

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getTasks()) {
                writer.println(toStringTask(task));
            }
            for (Epic epic : getEpics()) {
                writer.println(toStringEpic(epic));
            }

            for (SubTask subtask : getSubTasks()) {
                writer.println(toStringSubTask(subtask));
            }
            writer.println();
            writer.write(historyToString(getHistoryManager()));
            writer.println();
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }

    }

    private String toStringSubTask(SubTask subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getTitle() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId();
    }

    private String toStringEpic(Epic epic) {
        return epic.getId() + "," + epic.getTaskType() + "," + epic.getTitle() + "," + epic.getStatus() + "," + epic.getDescription();
    }

    private String toStringTask(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription();
    }

    @Override
    public void addTasks(Task task) {
        super.addTasks(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }


    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Task getEpicById(Integer id) {
        Task epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Task getSubtaskById(Integer id) {
        Task subTask = super.getSubtaskById(id);
        save();
        return subTask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }


    @Override
    public List<SubTask> getEpicSubtasks(Integer id) {
        return super.getEpicSubtasks(id);
    }

    @Override
    public void updateEpic(Epic epic, List<SubTask> listEpic) {
        super.updateEpic(epic, listEpic);
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public void deleteSubtasks(Integer id, Integer idEpic) {
        super.deleteSubtasks(id, idEpic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask, Integer idEpic, SubTask subTaskPrev) {
        super.updateSubtask(subTask, idEpic, subTaskPrev);
        save();
    }

    private String historyToString(HistoryManager historyManager) {
        List<Task> history = historyManager.getHistory();
        StringBuilder str = new StringBuilder();

        for (Task task : history) {
            str.append(task.getId()).append(",");
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    public void loadFromFile(File file) {
        try (BufferedReader readerCSV = new BufferedReader(new FileReader(file))) {
            String lineCSV = readerCSV.readLine();
            while (readerCSV.ready()) {

                lineCSV = readerCSV.readLine();
                if (lineCSV.isEmpty()) {
                    break;
                }
                Task task = historyFromString(lineCSV);

                if (task.getTaskType().toString().equals("SUBTASK")) {
                    addSubTask((SubTask) task);
                } else if (task.getTaskType().toString().equals("EPIC")) {
                    addEpic((Epic) task);
                } else {
                    addTasks(task);
                }
            }
            String lineWithHistory = readerCSV.readLine();
            for (Integer id : historyViewFromString(lineWithHistory)) {
                addToHistory(id);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Не удалось считать файл");
        }

    }

    static List<Integer> historyViewFromString(String value) {
        List<Integer> historyView = new ArrayList<>();
        if (value.isEmpty()) {
            List<String> id = List.of(value.split(","));
            for (String number : id) {
                historyView.add(Integer.valueOf(number));
            }
        }
        return historyView;
    }

    private Task historyFromString(String value) {
        List<String> line = List.of(value.split(","));
        if (line.get(1).equals("SUBTASK")) {
            SubTask subtask = new SubTask(line.get(2), line.get(4), Status.valueOf(line.get(3)), Integer.parseInt(line.get(5)));
            return subtask;
        } else if (line.get(1).equals("EPIC")) {
            Epic epic = new Epic(line.get(2), line.get(4), Status.valueOf(line.get(3)));
            return epic;
        } else {
            Task task = new Task(line.get(2), line.get(4), Status.valueOf(line.get(3)));
            return task;
        }
    }
}