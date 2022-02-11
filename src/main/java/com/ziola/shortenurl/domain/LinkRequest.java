package com.ziola.shortenurl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "links")
public class LinkRequest {

    private static final long EXPIRATION = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;
    @Column(name = "full_length_link")
    private String fullLengthLink;
    @Column(name = "shorten_link")
    private String shortenLink;
    @Column(name = "visits")
    private Long visits;
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    @Column(name = "password")
    private String password;

    private LocalDate calculateExpiryDate() {
        LocalDate dateOfCreation = LocalDate.now();
        return dateOfCreation.plusDays(EXPIRATION);
    }

    public LinkRequest() {
        expiryDate = calculateExpiryDate();
    }

    public void updateVisits() {
        this.visits = visits + 1;
    }
}
