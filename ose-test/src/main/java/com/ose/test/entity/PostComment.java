package com.ose.test.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;

@Entity
@Table(name = "post_comment")
public class PostComment extends BaseDTO {
    private static final long serialVersionUID = 6548871254774803255L;

    @Id
    private Long id;


    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable=false)
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
