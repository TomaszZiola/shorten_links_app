package com.ziola.shortenurl.repository;

import com.ziola.shortenurl.domain.LinkRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends CrudRepository<LinkRequest, Long> {

    LinkRequest findByShortenLink(String result);

    LinkRequest findByPassword(String password);
}
