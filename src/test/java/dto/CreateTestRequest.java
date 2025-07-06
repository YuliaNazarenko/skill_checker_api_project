package dto;

public class CreateTestRequest {
    private String name;
    private int intName;
    private String description;
    private String category;
    private int timeLimit;
    private int passingScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setIntName(int intName) {
        this.intName = intName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }
}
