package org.zerock;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebBoardReply;
import org.zerock.domain.WebReply;
import org.zerock.persistence.WebBoardReplyRepository;
import org.zerock.persistence.WebBoardRepository;
import org.zerock.persistence.WebReplyRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
public class WebBoardRepoTest {
	@Autowired
	WebBoardRepository repo;
	@Autowired
	WebBoardReplyRepository rrepo;
	@Autowired
	WebReplyRepository wrrepo;
	
	@Test
	public void insertDummis() {
		for(int i =0; i<100; i++) {
			WebBoard board = new WebBoard();
			board.setTitle("title" + (i+1));
			board.setContent("content..." + (i+1));
			board.setWriter("user0" + (i%10));
			ArrayList<WebBoardReply> list = new ArrayList();
			for(int j =0; j <10; j++) {
				WebBoardReply reply = new WebBoardReply();
				reply.setReply("reply....");
				reply.setReplyer("user0" + j);
				reply.setBoard(board);
				list.add(reply);
			}
			//board.setReplies(list);
			repo.save(board);
		}
	}
	
	@Test
	public void testPridicate() {
		Pageable pageable = PageRequest.of(6, 15, Sort.Direction.DESC,"bno");
		Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"),pageable);
		result.getContent().forEach(board->System.out.println(board));
	}
	
	@Test
	public void insertReplyDummies() {
		repo.findAll().forEach(board -> {
			for(int i =0; i <10; i++) {
				WebReply reply = new WebReply();
				reply.setReply("reply....");
				reply.setReplyer("user0" + i);
				reply.setBoard(board);
				wrrepo.save(reply);
			}	
		});
		
	}
}
