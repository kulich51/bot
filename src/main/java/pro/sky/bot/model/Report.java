package pro.sky.bot.model;

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

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(name = "file_id")
    private String fileId;

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
        this.fileId = fileId;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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
                ", fileId='" + fileId + '\'' +
                ", date=" + date +
                ", textReport='" + textReport + '\'' +
                ", isAccepted=" + isAccepted +
                '}';
    }
}
