package com.web6.server.service;

import com.web6.server.dto.MovieArticleDTO;
import com.web6.server.repository.MovieArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieArticleService {

    @Autowired
    private MovieArticleRepository movieArticleRepository;

    public List<MovieArticleDTO> getOrderByGradeCountDesc() {
        PageRequest pageRequest = PageRequest.of(0, 500); // 한 페이지당 50개의 항목을 가져옴
        return movieArticleRepository.findOrderByGradeCountDesc(pageRequest).getContent();
    }
}
