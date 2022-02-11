package com.ziola.shortenurl.controller;

import com.ziola.shortenurl.domain.LinkRequest;
import com.ziola.shortenurl.dto.LinkResponse;
import com.ziola.shortenurl.dto.Link;
import com.ziola.shortenurl.dto.LinkWithPassword;
import com.ziola.shortenurl.exception.LinkException;
import com.ziola.shortenurl.exception.LinkWrongFormatException;
import com.ziola.shortenurl.repository.LinkRepository;
import com.ziola.shortenurl.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
@RequiredArgsConstructor
public class ShortenLinkRestController {

    private final LinkService shortenLinkService;
    private final LinkRepository repository;

    @PostMapping(value = "/shorten")
    public LinkWithPassword shortLink(@RequestBody Link link) {
        checkIfCorrect(link);
        LinkRequest request = shortenLinkService.createShortenLink(link.getLink());
        LinkWithPassword shortenLink = new LinkWithPassword();
        shortenLink.setLink(request.getShortenLink());
        shortenLink.setPassword(request.getPassword());
        return shortenLink;
    }

    private void checkIfCorrect(Link link) {
        String[] schemes = {"https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        String linkToSite = link.getLink();
        if (isBlank(linkToSite)){
            throw new LinkException("Link cannot be empty!");
        } else if (!urlValidator.getInstance().isValid(link.getLink())) {
            throw new LinkWrongFormatException("Bad link's format! Make sure to start from https");
        }
    }

    @GetMapping(value = "/{link}")
    public LinkResponse fullLink(@PathVariable("link") String link, HttpServletResponse responseServlet)  {
        LinkRequest request = shortenLinkService.findAndUpdate(link);
        LinkResponse response = new LinkResponse();
        response.setFullLengthLink(request.getFullLengthLink());
        response.setVisits(request.getVisits());
        try {
            responseServlet.sendRedirect(request.getFullLengthLink());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @DeleteMapping("/{password}")
    public void deleteShortenLink(@PathVariable String password) {
        LinkRequest request = repository.findByPassword(password);
        repository.delete(request);
    }
}
