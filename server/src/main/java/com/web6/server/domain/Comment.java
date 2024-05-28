/*
댓글 정보를 저장하는 테이블로 Member 테이블의 ID를 WRITER_ID로 참조한다.

 */
package com.web6.server.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
public class Comment {
    @Id                 //ID 필드를 기본 키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //ID 값은 데이터베이스에 의해 자동으로 생성됨
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    // WRITER_ID 필드는 Member 테이블의 ID를 참조하는 외래 키
    @ManyToOne
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private Member writer;

    @Column(name = "CONTENT", nullable = false, length = 255) // 여기에서 VARCHAR 길이 설정
    private String content;

    @Column(name = "CREATE_DATE", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "EDIT", nullable = false)
    private boolean edit;

    // Constructors, getters and setters
    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getWriter() {
        return writer;
    }

    public void setWriter(Member writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreaeDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
