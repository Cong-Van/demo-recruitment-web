package com.funix.prj_321x.asm02.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "recruitment")
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "description")
    private String description;

    @Column(name = "experience")
    private String experience;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "salary")
    private String salary;

    @Column(name = "status")
    private int status;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "view")
    private int view;

    @Column(name = "deadline")
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Liên kết các Recruitment cho Company đăng tuyển
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "company_id")
    private Company company;

    // Liên kết các User là Candidate khi follow Recruitment
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "save_job",
            joinColumns = @JoinColumn(name = "recruitment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    // Liên kết với các ApplyPost ứng tuyển cho Recruitment
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recruitment")
    private List<ApplyPost> applyPosts;

    public  Recruitment() {
        this.createdAt = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ApplyPost> getApplyPosts() {
        return applyPosts;
    }

    public void setApplyPosts(List<ApplyPost> applyPosts) {
        this.applyPosts = applyPosts;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm02.entity.Recruitment{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", created_at=" + createdAt +
                ", description='" + description + '\'' +
                ", experience='" + experience + '\'' +
                ", quantity=" + quantity +
                ", salary='" + salary + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", view=" + view +
                ", deadline='" + deadline + '\'' +
                ", category=" + category +
                ", company=" + company +
                '}';
    }
}
