package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebBoardReply;
import org.zerock.persistence.WebBoardReplyRepository;
import org.zerock.persistence.WebBoardRepository;
import org.zerock.vo.PageVO;

@Controller
@RequestMapping("/reply")
public class WebBoardReplyController {
	@Autowired
	WebBoardRepository brepo;
	@Autowired
	WebBoardReplyRepository brrepo;
	
	@PostMapping("/register")
	public String register(String replyer, String reply,
			Long bno, PageVO vo, 
			Model model, RedirectAttributes rttr) {
		brepo.findById(bno).ifPresent(b->{
			WebBoardReply webreply = new WebBoardReply();
			webreply.setReply(reply);
			webreply.setReplyer(replyer);
			webreply.setBoard(b);
			brrepo.save(webreply);
		});
		
		rttr.addAttribute("bno", bno);
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());
		rttr.addFlashAttribute("msg","reg_success");
		return "redirect:/boards/view";
	}
	
	@PostMapping("/modify")
	public String modify(WebBoardReply reply, Long bno, 
			@ModelAttribute("vo")PageVO vo, Model model, RedirectAttributes rttr) {
		brrepo.findById(reply.getRno()).ifPresent(origin -> {
			origin.setReply(reply.getReply());
			origin.setReplyer(reply.getReplyer());
			brrepo.save(origin);
		});
		
		rttr.addFlashAttribute("msg", "mod_success");
		return "redirect:/boards/view";
	}
}
