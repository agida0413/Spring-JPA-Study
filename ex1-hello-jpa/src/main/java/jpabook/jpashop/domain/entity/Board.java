package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Board extends BaseEntity{
    @Column(name = "content")
    @Lob
    private String content;
    @Column(name="title" , length = 200 )
    private String title;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
