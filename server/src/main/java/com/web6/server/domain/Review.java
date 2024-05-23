/*
Member 엔티티를 참조하여 리뷰 작성자 정보를 연결한다.
연결은 ManyToOne 관계로 설정되고, Review 테이블의 WRITER_ID 필드는 Member 테이블의 ID 필드를 참조한다.
이를 통해 각 리뷰가 어떤 사용자에 의해 작성되었는지를 추적할 수 있다.

CONTENT 필드를 TEXT 타입으로 명시적으로 정의하기 위해,
columnDefinition = "TEXT" 속성을 사용함. 이는 대용량 텍스트가 필요할 때 유용하다.
 */
package com.web6.server.domain;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private Member writer; // 사용자 엔티티 참조 변경

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "GRADE", nullable = false)
    private double grade;

    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;

    @Column(name = "EDIT", nullable = false)
    private boolean edit;

    @Column(name = "SPOILER", nullable = false)
    private boolean spoiler;

    // Constructors, getters, and setters
    public Review() {
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

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }
}



