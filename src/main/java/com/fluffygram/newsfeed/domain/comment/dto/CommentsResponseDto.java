package com.fluffygram.newsfeed.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fluffygram.newsfeed.domain.comment.entity.Comments;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentsResponseDto {
    private final Long id;
    private final Long BoardId;
    private final String comment;
    private final String userNickname;
    private final LocalDateTime createdat;
    private final LocalDateTime modifyat;


    public CommentsResponseDto(Comments comments) {
        this.id = comments.getId();
        this.BoardId = comments.getBoard().getId();
        this.userNickname = comments.getComment();
        this.comment = comments.getComment();
        this.createdat = comments.getCreatedAt();
        this.modifyat = comments.getModifiedAt();
    }
    public static CommentsResponseDto toDto(Comments comments) {
        return new CommentsResponseDto(comments.getId(), comments.getBoard().getId(), comments.getComment(), comments.getUser().getUserNickname(),comments.getCreatedAt(), comments.getModifiedAt());
    }
}