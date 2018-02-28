package org.zerock.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="tbl_freeboards")
@EqualsAndHashCode(of="bno")
public class FreeBoard {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bno;
	private String title;
	private String writer;
	private String content;
	//http://jdm.kr/blog/141 Onetomany일때 정의할수있는 속정 설명되어있음
	/*
	 FreeBoardReply의 board값에 FreeBoard가 얽매여 있음.
	 게시판에 댓글이 있는경우 삭제불가. 즉 게시판은 댓글에 매여있는거
	 양방향관계일때 매여있는쪽이 관계의 주체가 되며, 주체가 되는쪽에서 mappedBy를 표시해야함
	*/
	
	@CreationTimestamp
	private Timestamp regdate;
	@UpdateTimestamp
	private Timestamp updatedate;
	
}
