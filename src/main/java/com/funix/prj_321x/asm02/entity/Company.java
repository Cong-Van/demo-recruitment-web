package com.funix.prj_321x.asm02.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "logo")
    private String logo;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private int status;

    // Liên kết Company với người đại diện của công ty là User với vai trò là HR
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Liên kết với các User là Candidate khi follow Company
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "follow_company",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    // Liên kết các Recruitment tương ứng của Company
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Recruitment> recruitments;

    public Company() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Recruitment> getRecruitments() {
        return recruitments;
    }

    public void setRecruitments(List<Recruitment> recruitments) {
        this.recruitments = recruitments;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm02.entity.Company{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", logo='" + logo + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", user=" + user +
                '}';
    }
}
