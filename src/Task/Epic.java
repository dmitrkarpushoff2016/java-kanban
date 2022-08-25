package Task;

import Enum.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subtasks = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks='" + subtasks + '\''+
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\''+
                ", id='" + getId() + '\''+
                '}';
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void addSubtasks(SubTask subTask) {
        this.subtasks.add(subTask);
    }

    public void setAllSubtasks(List<SubTask> listEpic) {
        this.subtasks.addAll(listEpic);
    }

}