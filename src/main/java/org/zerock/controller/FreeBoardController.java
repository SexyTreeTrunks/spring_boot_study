package org.zerock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.zerock.domain.FreeBoard;
import org.zerock.persistence.FreeBoardRepository;

@Controller
public class FreeBoardController {
	@Autowired
	FreeBoardRepository fbrepo;
	
	@GetMapping("/free-board")
	public void freeBoard(@RequestParam(value="pNo", defaultValue="1") Integer pNo ,Model model) {
		Pageable page = PageRequest.of(pNo - 1, 10, Sort.Direction.DESC, "bno");
		Page<FreeBoard> pages = fbrepo.findAll(page);
		model.addAttribute("contents", pages.getContent());
		model.addAttribute("pages", pages);
	}
	
	@GetMapping("/sample1")
	public void sample1(Model model) {
		model.addAttribute("greeting","Hello word");
	}
	
	@GetMapping("/sample2")
	public void sample2(Model model) {
		
	}
}
