package co.edu.javeriana.as.personapp.model.response;

public class StudyResponse {
    private Integer idPerson;
    private Integer idProfession;
    private String graduationDate;
    private String universityName;
    private String database;

    public StudyResponse() {}

    public StudyResponse(Integer idPerson, Integer idProfession, String graduationDate, String universityName, String database) {
        this.idPerson = idPerson;
        this.idProfession = idProfession;
        this.graduationDate = graduationDate;
        this.universityName = universityName;
        this.database = database;
    }

    public Integer getIdPerson() { return idPerson; }
    public void setIdPerson(Integer idPerson) { this.idPerson = idPerson; }

    public Integer getIdProfession() { return idProfession; }
    public void setIdProfession(Integer idProfession) { this.idProfession = idProfession; }

    public String getGraduationDate() { return graduationDate; }
    public void setGraduationDate(String graduationDate) { this.graduationDate = graduationDate; }

    public String getUniversityName() { return universityName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
}