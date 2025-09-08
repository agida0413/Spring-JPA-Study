package jpabook.jpashop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reply extends BaseEntity{
    @Column(name = "reply" , length = 300)
    private String reply;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
