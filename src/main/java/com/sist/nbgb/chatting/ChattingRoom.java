package com.sist.nbgb.chatting;

import com.sist.nbgb.entity.Instructors;
import com.sist.nbgb.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChattingRoom 
{
	private String chatId;
	
	private User userId;
	
	private Instructors instructorId;
	    
    @Builder
    public ChattingRoom(String chatId, User userId, Instructors instructorId) {
        this.chatId = chatId;
        this.userId = userId;
        this.instructorId = instructorId;
    }
}
