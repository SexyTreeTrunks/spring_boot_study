package org.zerock.controller;

import java.util.List;

import javax.jdo.annotations.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebReply;
import org.zerock.persistence.WebReplyRepository;

@RestController
@RequestMapping("/replies/*")
public class WebReplyController {
	@Autowired
	private WebReplyRepository replyrepo;
	
	@Transactional
	@PostMapping("/{bno}")
	public ResponseEntity<WebReply> addReply(@PathVariable("bno")Long bno, @RequestBody WebReply reply) {
		WebBoard board = new WebBoard();
		board.setBno(bno);
		reply.setBoard(board);
		replyrepo.save(reply);
		return new ResponseEntity<>(reply, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{bno}")
	public ResponseEntity<List<WebReply>> getReply(@PathVariable("bno")Long bno) {
		WebBoard board = new WebBoard();
		board.setBno(bno);
		return new ResponseEntity<>(getRepliesByBoard(board), HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping("/{bno}")
	public ResponseEntity<List<WebReply>> modifyReply(
			@PathVariable("bno")Long bno,
			@RequestBody WebReply reply) {
		replyrepo.findById(reply.getRno()).ifPresent(origin->{
			origin.setReply(reply.getReply());
			replyrepo.save(origin);
		});
		WebBoard board = new WebBoard();
		board.setBno(bno);
		return new ResponseEntity<>(getRepliesByBoard(board), HttpStatus.CREATED);
	}
	
	@Transactional
	@DeleteMapping("/{bno}/{rno}")
	public ResponseEntity<Long> deleteReply(
			@PathVariable("bno")Long bno,
			@PathVariable("rno")Long rno) {
		replyrepo.deleteById(rno);
		WebBoard board = new WebBoard();
		board.setBno(bno);
		return new ResponseEntity<Long>(rno, HttpStatus.OK);
	}
	
	private List<WebReply> getRepliesByBoard(WebBoard board) throws RuntimeException{
		return replyrepo.findByBoard(board);
	}
}