/*
댓글수를 기준으로 5개의 Article(title, gradeCount)을 반환.
 */
package com.web6.server.controller;

import com.web6.server.dto.MovieArticleDTO;
import com.web6.server.service.MovieArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieArticleController {

    @Autowired
    private MovieArticleService movieArticleService;

    @GetMapping("/Top5OrderByGradeCountDesc")
    public List<MovieArticleDTO> getTop5OrderByGradeCountDesc() {
        return movieArticleService.getTop5OrderByGradeCountDesc();
    }
}
