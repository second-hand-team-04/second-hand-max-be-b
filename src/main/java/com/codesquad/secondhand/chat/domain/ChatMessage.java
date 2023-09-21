package com.codesquad.secondhand.chat.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class ChatMessage {

	//FIXME entity 로 하는 것이 최선??
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private MessageType type;

	@OneToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	private String content;

	@CreatedDate
	private LocalDateTime createdAt;

	private Boolean isRead;

	public ChatMessage(MessageType type, User sender, String content) {
		this.type = type;
		this.sender = sender;
		this.content = content;
	}

	public static ChatMessage of(MessageType type, User sender, String content) {
		return new ChatMessage(type, sender, content);
	}
}
