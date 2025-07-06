package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateTestResponse {
    private String id;
    private String organizationId;
    private String name;
    private String description;
    private String createdBy;
    private String timeLimit;
    @JsonProperty("isActive")
    private boolean isActive;
    private int passingScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    @Override
    public String toString() {
        return "CreateTestResponse{" +
                "id='" + id + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", timeLimit='" + timeLimit + '\'' +
                ", isActive=" + isActive +
                ", passingScore=" + passingScore +
                '}';
    }
}
