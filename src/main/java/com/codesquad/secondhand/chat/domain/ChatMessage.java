package com.codesquad.secondhand.chat.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private ChatRoom chatRoom;

	@OneToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	private String content;

	@CreatedDate
	private LocalDateTime createdAt;

	private boolean isRead;

	public ChatMessage(ChatRoom chatRoom, User sender, String content) {
		this.chatRoom = chatRoom;
		this.sender = sender;
		this.content = content;
	}

	public static ChatMessage of(ChatRoom chatRoom, User sender, String content) {
		return new ChatMessage(chatRoom, sender, content);
	}
}
