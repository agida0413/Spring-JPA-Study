package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;

@Entity
public class BoardDetail extends BaseEntity{
    @Column(name = "detail_content")
    @Lob
    private String detailContent;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
