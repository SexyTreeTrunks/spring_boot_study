package org.zerock.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebReply;

public interface WebReplyRepository extends CrudRepository<WebReply, Long>{
	@Query("select r from WebReply r where r.board=?1 ORDER BY r.rno DESC")
	public List<WebReply> findByBoard(WebBoard board);
}
