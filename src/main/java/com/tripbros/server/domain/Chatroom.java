package com.tripbros.server.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Chatroom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	private boolean status;
	private LocalDateTime createAt;

	@Builder
	public Chatroom(Schedule schedule, boolean status, LocalDateTime createAt) {
		this.schedule = schedule;
		this.status = status;
		this.createAt = createAt;
	}
}
