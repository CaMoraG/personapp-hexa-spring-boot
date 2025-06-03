package co.edu.javeriana.as.personapp.model.response;

public class ProfessionResponse {
    private Integer identification;
    private String name;
    private String description;
    private String database;

    public ProfessionResponse() {}

    public ProfessionResponse(Integer identification, String name, String description, String database) {
        this.identification = identification;
        this.name = name;
        this.description = description;
        this.database = database;
    }

    public Integer getIdentification() { return identification; }
    public void setIdentification(Integer identification) { this.identification = identification; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
}