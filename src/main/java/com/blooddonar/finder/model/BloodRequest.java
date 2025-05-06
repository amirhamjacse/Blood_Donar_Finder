package com.blooddonar.finder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.blooddonar.finder.model.User.BloodGroup;
import com.blooddonar.finder.model.User;
import com.blooddonar.finder.model.Status;

@Entity
@Table(name = "blood_request")
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bg_group")
    private BloodGroup bgGroup;

    @ManyToOne
    @JoinColumn(name = "requester_by_id", referencedColumnName = "id")
    private User requesterBy;

    @ManyToOne
    @JoinColumn(name = "requester_to_id", referencedColumnName = "id")
    private User requesterTo;

    private String city;

    private String district;

    private String message;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Enumerated(EnumType.STRING)
    private Status status;

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBgGroup(BloodGroup bgGroup) {
        this.bgGroup = bgGroup;
    }

    public void setRequesterId(Long requesterToId, Long requesterById) {
         User toUser = new User();
        toUser.setId(requesterToId);
        this.requesterTo = toUser;

        User byUser = new User();
        byUser.setId(requesterById);
        this.requesterBy = byUser;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BloodGroup getBgGroup() {
        return bgGroup;
    }

    public User getRequesterBy() {
        return requesterBy;
    }

    public User getRequesterTo() {
        return requesterTo;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public Status getStatus() {
        return status;
    }

}
