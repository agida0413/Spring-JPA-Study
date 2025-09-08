package jpabook.jpashop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FREE_BOARD")
public class FreeBoard extends Board{
    @Column(name ="free_content",length = 300)
    private String freeContent;

    public void setFreeContent(String freeContent) {
        this.freeContent = freeContent;
    }

    public String getFreeContent() {
        return freeContent;
    }
}
