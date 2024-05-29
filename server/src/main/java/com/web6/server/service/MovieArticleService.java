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

    public List<MovieArticleDTO> getTop5OrderByGradeCountDesc() {
        PageRequest pageRequest = PageRequest.of(0, 5); // 첫 페이지에서 5개의 항목을 가져옴
        return movieArticleRepository.findTop5OrderByGradeCountDesc(pageRequest).getContent();
    }
}
