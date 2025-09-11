package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    @Column(name ="user_id",length = 40)
    private String userId;
    @Column(name ="email",length = 300,unique = true)
    private String email;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Board> board;

    public List<Board> getBoard() {
        return board;
    }

    public void addBoard(Board board){
        this.board.add(board);
        board.setMember(this);
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
