package com.funix.prj_321x.asm02.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "apply_post")
public class ApplyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "cv_name")
    private String cvName;

    @Column(name = "status")
    private int status;

    @Column(name = "text")
    private String text;

    // Liên kết các ApplyPost tương ứng với Recruitment
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    // Liên kết các ApplyPost tương ứng với User là Candidate
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public ApplyPost() {
        this.createdAt = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getCvName() {
        return cvName;
    }

    public void setCvName(String cvName) {
        this.cvName = cvName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Recruitment getRecruitment() {
        return recruitment;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm02.entity.ApplyPost{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", nameCV='" + cvName + '\'' +
                ", status=" + status +
                ", text='" + text + '\'' +
                ", recruitment=" + recruitment +
                ", user=" + user +
                '}';
    }
}
