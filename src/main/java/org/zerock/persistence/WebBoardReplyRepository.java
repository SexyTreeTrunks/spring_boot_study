package org.zerock.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.WebBoardReply;

public interface WebBoardReplyRepository extends CrudRepository<WebBoardReply, Long>{
	
}
