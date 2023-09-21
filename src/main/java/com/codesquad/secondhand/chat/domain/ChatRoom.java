package com.codesquad.secondhand.chat.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@OneToOne
	@JoinColumn(name = "buyer_id")
	private User buyer;

	@OneToMany
	private List<ChatMessage> chatMessages;

	@OneToMany
	private List<ChatRoomUser> participants;

	public ChatRoom(Item item, User buyer) {
		this.item = item;
		this.buyer = buyer;
	}

	public static ChatRoom of(Item item, User buyer) {
		return new ChatRoom(item, buyer);
	}
}
