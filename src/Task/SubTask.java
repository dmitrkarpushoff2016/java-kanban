package Task;

import Enum.Status;

public class SubTask extends Task {

    private int epicId;// id главной задачи

    public SubTask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" + '\'' +
                "epicId=" + epicId + '\''+
                ", title='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id='" + getId() + '\''+ '\n' +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }

}