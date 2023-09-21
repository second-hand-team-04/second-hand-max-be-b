package com.codesquad.secondhand.chat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.codesquad.secondhand.user.domain.User;

import lombok.Getter;

@Entity
@Getter
public class ChatRoomUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chatroom_id")
	private ChatRoom chatRoom;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
