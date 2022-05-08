package com.ziola.shortenurl.service;

import com.ziola.shortenurl.domain.LinkRequest;
import com.ziola.shortenurl.dto.Link;
import com.ziola.shortenurl.exception.LinkException;
import com.ziola.shortenurl.exception.LinkWrongFormatException;
import com.ziola.shortenurl.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class LinkService{
    
    private final LinkRepository shortenLinkRepository;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public LinkRequest createShortenLink(String link) {
        LinkRequest shortenLink = new LinkRequest();
        shortenLink.setFullLengthLink(link);
        shortenLink.setVisits(0L);
        shortenLink.setShortenLink(generateShortenLink(6));
        shortenLink.setPassword(generatePassword(3));
        return shortenLinkRepository.save(shortenLink);
    }

    public LinkRequest findAndUpdate(String link) {
        LinkRequest request = shortenLinkRepository.findByShortenLink(link);
        if (request == null) {
            throw new LinkException("Nie ma takiego skróconego linku!");
        }else {
            request.updateVisits();
            return shortenLinkRepository.save(request);
        }
    }

    public String generatePassword(int passwordLength) {
        String password = randomAlphaNumeric(passwordLength);
        LinkRequest temp = shortenLinkRepository.findByPassword(password);
        while (temp != null) {
            password = randomAlphaNumeric(passwordLength);
            temp = shortenLinkRepository.findByPassword(password);
        }
        return password;
    }

    private String generateShortenLink(int shortenLinkLength) {
        String result = randomAlphaNumeric(shortenLinkLength);
        LinkRequest temp = shortenLinkRepository.findByShortenLink(result);
        while (temp != null) {
            result = randomAlphaNumeric(shortenLinkLength);
            temp = shortenLinkRepository.findByShortenLink(result);
        }
        return result;
    }

    private static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public void checkIfCorrect(Link link) {
        String[] schemes = {"https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        String linkToSite = link.getLink();
        if (isBlank(linkToSite)){
            throw new LinkException("Link cannot be empty!");
        } else if (!urlValidator.getInstance().isValid(link.getLink())) {
            throw new LinkWrongFormatException("Bad link's format! Make sure to start from https");
        }
    }
}
