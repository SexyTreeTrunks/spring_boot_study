package org.zerock.domain;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.zerock.domain.WebBoard;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="board")
@Entity
@Table(name="webboards_replies")
@EqualsAndHashCode(of="rno")
public class WebBoardReply {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long rno;
	private String reply;
	private String replyer;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private WebBoard board;
	
	@CreationTimestamp
	private Timestamp regdate;
	@UpdateTimestamp
	private Timestamp updatedate;
}