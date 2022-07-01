package pro.sky.bot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonBackReference
    private Pet pet;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "date")
    private Date date;

    @Column(name = "text_report")
    private String textReport;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    public Report() {
    }

    public Report(Long id, Long userId, Pet pet, String fileId, Date date, String textReport, Boolean isAccepted) {
        this.id = id;
        this.userId = userId;
        this.pet = pet;
        this.filePath = fileId;
        this.date = date;
        this.textReport = textReport;
        this.isAccepted = isAccepted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String fileId) {
        this.filePath = fileId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTextReport() {
        return textReport;
    }

    public void setTextReport(String textReport) {
        this.textReport = textReport;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userId=" + userId +
                ", pet=" + pet +
                ", fileId='" + filePath + '\'' +
                ", date=" + date +
                ", textReport='" + textReport + '\'' +
                ", isAccepted=" + isAccepted +
                '}';
    }
}
