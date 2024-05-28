package com.web6.server.service;

import com.web6.server.dto.MovieArticleDTO;
import com.web6.server.repository.MovieArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieArticleService {

    @Autowired
    private MovieArticleRepository movieArticleRepository;

    public List<MovieArticleDTO> getAllOrderByGradeCountDesc() {
        return movieArticleRepository.findAllOrderByGradeCountDesc();
    }
}
