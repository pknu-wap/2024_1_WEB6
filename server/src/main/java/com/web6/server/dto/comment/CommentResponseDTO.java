package com.web6.server.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web6.server.domain.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDTO {
    private Long id;
    private String nickname;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
    private boolean edit;

    public CommentResponseDTO() {}
    //create
    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getWriter().getNickname();
        this.content = comment.getContent();
        this.createDate = comment.getCreateDate();
        this.edit = comment.isEdit();
    }
    //update
    public CommentResponseDTO(Long id, String nickname, String content, LocalDateTime createDate, boolean edit) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
        this.edit = edit;
    }
}
