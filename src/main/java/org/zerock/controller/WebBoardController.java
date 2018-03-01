package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.WebBoard;
import org.zerock.persistence.WebBoardRepository;
import org.zerock.vo.MyPageMaker;
import org.zerock.vo.PageMaker;
import org.zerock.vo.PageVO;

@Controller
@RequestMapping("/boards/")
public class WebBoardController {
	@Autowired
	private WebBoardRepository repo;
	
	@GetMapping("/list") //ModelAttribute하면 model에 pageVO란 이름으로 attribute전달됨
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
		Pageable page = vo.makePageable(0, "bno");
		Page<WebBoard> result= repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
		
		model.addAttribute("result", new PageMaker(result));
	}
	
	@GetMapping("/mylist")
	public void mylist(@ModelAttribute("pageVO") PageVO vo, Model model) {
		Pageable page = vo.makePageable(0, "bno");
		Page<WebBoard> result= repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
		
		model.addAttribute("result", new MyPageMaker(result));
	}
	
	@GetMapping("/register")
	public void registerGET(@ModelAttribute("vo")WebBoard vo) {
		
	}
	
	@PostMapping("/register")
	public String registerPOST(@ModelAttribute("vo")WebBoard vo, RedirectAttributes rttr) {//RedirectAttributes url로는 보이지 않는 문자열 생성
		repo.save(vo);
		rttr.addFlashAttribute("msg","reg_success");
		return "redirect:/boards/list"; //post-redirect-get
	}
	
	@GetMapping("/view")
	public void view(Long bno, @ModelAttribute("pageVO")PageVO vo, Model model) {
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}
	
	@GetMapping("/modify")
	public void modify(Long bno, @ModelAttribute("pageVO")PageVO vo, Model model) {
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}
	
	@PostMapping("/modify")
	public String modify(WebBoard board, PageVO vo, RedirectAttributes rttr) {
		repo.findById(board.getBno()).ifPresent(origin -> {
			origin.setTitle(board.getTitle());
			origin.setContent(board.getContent());
			repo.save(origin);
			rttr.addFlashAttribute("msg","success");
			rttr.addAttribute("bno",origin.getBno());
		});
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());
		return "redirect:/boards/view";
	}
	
	@PostMapping("/delete")
	public String delete(Long bno, PageVO vo, RedirectAttributes rttr) {
		repo.deleteById(bno);
		rttr.addFlashAttribute("msg", "del_success");
		rttr.addAttribute("page",vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());
		return "redirect:/boards/list";
	}
}
