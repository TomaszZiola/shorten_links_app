package com.ziola.shortenurl.service;

import com.ziola.shortenurl.domain.LinkRequest;
import com.ziola.shortenurl.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class CheckExpiredLinksService {

    private final LinkRepository repository;

    @Scheduled(cron = "0 0 * * * ?")
    public void updateLinks() {
        Iterable<LinkRequest> allLinks = repository.findAll();
        for (LinkRequest shortenLink : allLinks) {
            if (shortenLink.getExpiryDate().isAfter(LocalDate.now())) {
                repository.delete(shortenLink);
            }
        }
    }
}
