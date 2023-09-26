package com.codesquad.secondhand.chat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.codesquad.secondhand.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private ChatRoom chatRoom;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public ChatRoomUser(ChatRoom chatRoom, User user) {
		this.chatRoom = chatRoom;
		this.user = user;
	}

	public static ChatRoomUser of(ChatRoom chatRoom, User user) {
		return new ChatRoomUser(chatRoom, user);
	}
}
