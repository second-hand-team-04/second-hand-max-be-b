package com.codesquad.secondhand.chat.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@OneToMany
	private List<ChatMessage> chatMessages;

	@Embedded
	private ChatRoomMembers chatRoomMembers = new ChatRoomMembers();

	public ChatRoom(Item item, User user) {
		this.item = item;
		this.chatRoomMembers.addParticipant(ChatRoomUser.of(this, user));
	}

	public static ChatRoom of(Item item, User buyer) {
		return new ChatRoom(item, buyer);
	}

	public void enterByUser(User user) {
		// TODO
		// 1. 사용자가 채팅방의 참여자인지 검증한다
		// 2. 채팅방에 접속한다
		// (3.) 채팅방의 메세지를 읽음처리한다
		// chatRoomMembers.contains(user);
		// chatMessages.readByUser(user);
	}
}
