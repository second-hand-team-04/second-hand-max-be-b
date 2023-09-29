package com.codesquad.secondhand.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.common.exception.chat.ChatRoomParticipantNotIncludeException;
import com.codesquad.secondhand.user.domain.User;

@Embeddable
public class ChatRoomMembers {

	private static final int MAX_CHATTER_COUNT = 2;

	private static final int MIN_CHATTER_COUNT = 1;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatRoomUser> participants = new ArrayList<>();

	public void addParticipant(ChatRoomUser chatRoomUser) {
		if (!participants.contains(chatRoomUser)) {
			participants.add(chatRoomUser);
		}
	}

	public void removeParticipant(User user) {
		participants.remove(findParticipantByUser(user));
	}

	private ChatRoomUser findParticipantByUser(User user) {
		return participants.stream()
			.filter(chatter -> chatter.getUser().equalsId(user.getId()))
			.findAny()
			.orElseThrow(ChatRoomParticipantNotIncludeException::new);
	}

}
