package org.zerock;

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
import org.zerock.persistence.WebBoardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
public class WebBoardRepoTest {
	@Autowired
	WebBoardRepository repo;
	
	@Test
	public void insertDummis() {
		for(int i =0; i<100; i++) {
			WebBoard board = new WebBoard();
			board.setTitle("title" + (i+1));
			board.setContent("content..." + (i+1));
			board.setWriter("user0" + (i%10));
			repo.save(board);
		}
	}
	
	@Test
	public void testPridicate() {
		Pageable pageable = PageRequest.of(6, 15, Sort.Direction.DESC,"bno");
		Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"),pageable);
		result.getContent().forEach(board->System.out.println(board));
	}
}
