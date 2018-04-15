package org.zerock.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageMaker;
import org.zerock.service.BoardService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board/*")  //  WEB-INF/views/board로
public class BoardCotroller {

	private static final Logger logger = LoggerFactory.getLogger(BoardCotroller.class);
	
	@Inject
	private BoardService service;
	
	@RequestMapping(value = "/register" , method = RequestMethod.GET) //  WEB-INF/views/board/regoster 찾기
	public void registerGET(BoardVO board, Model model)throws Exception {
		logger.info("register get.......");
	}
	
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public String registPOST(BoardVO board, Model model)throws Exception {
//		logger.info("regist post.......");
//		logger.info(board.toString());
//		
//		service.regist(board);
//		
//		model.addAttribute("result","success");
//		
//		return "/board/success";		//success.jsp 로 	
//	}
		
		@RequestMapping(value = "/register", method = RequestMethod.POST)
		public String registPOST(BoardVO board, RedirectAttributes rttr)throws Exception {
//		public String registPOST(BoardVO board, Model model)throws Exception {
			logger.info("regist post.......");
			logger.info(board.toString());
			
			service.regist(board);
			
//			model.addAttribute("result","success");  //uri에 표시가 남기때문에 아래 방식으로 대체
			rttr.addFlashAttribute("msg","success");
			
			
			return "redirect:/board/listAll";
		}	
		
		@RequestMapping(value = "/listAll", method = RequestMethod.GET)
		public void listAll(Model model)throws Exception {
			logger.info("show all list.......");
			model.addAttribute("list", service.listAll()); //게시글 전체 불러오기
		}
		
		@RequestMapping(value = "/read", method = RequestMethod.GET)
		public void read(@RequestParam("bno")int  bno, Model model)throws Exception {
			
			model.addAttribute(service.read(bno)); //게시글 클릭하여 보기
		}
		
//		@RequestMapping(value = "/remove", method = RequestMethod.POST)
//		public String remove(@RequestParam("bno")int  bno, RedirectAttributes rttr)throws Exception {
//			
//			service.remove(bno);
//			
//			rttr.addFlashAttribute("msg", "success");
//			return "redirect:/board/listAll"; //게시글 삭제후 리다이렉트 listAll
//		}
		
		@RequestMapping(value = "/removePage", method = RequestMethod.POST)
		public String remove(@RequestParam("bno")int  bno, Criteria cri, RedirectAttributes rttr)throws Exception {
			
			service.remove(bno);
			
			rttr.addAttribute("page", cri.getPage());
			rttr.addAttribute("perPageNum", cri.getPerPageNum());
			rttr.addFlashAttribute("msg", "success");
			return "redirect:/board/listPage"; //게시글 삭제후 리다이렉트 listAll
		}
		
		@RequestMapping(value = "/modify", method = RequestMethod.GET)
		public void modifyGET(int bno,Model model)throws Exception {
			
			model.addAttribute(service.read(bno)); //수정
		}
		
		@RequestMapping(value = "/modify", method = RequestMethod.POST)
		public String modifyPOST(BoardVO  board, RedirectAttributes rttr)throws Exception {
			
			logger.info("mod post..........");
			
			service.modify(board);
			
			rttr.addFlashAttribute("msg", "success");
			return "redirect:/board/listAll"; //
		}
		
		@RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
		public void modifyPagingGET(@RequestParam("bno")int bno, @ModelAttribute("cri") Criteria cri, Model model)throws Exception {
			
			model.addAttribute(service.read(bno)); 
		}
		
		@RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
		public String modifyPagingPOST(BoardVO board, Criteria cri, RedirectAttributes rttr)throws Exception {
			
			service.modify(board);
			
			rttr.addFlashAttribute("page", cri.getPage());
			rttr.addFlashAttribute("perPageNum", cri.getPerPageNum());
			rttr.addFlashAttribute("msg", "SUCCESS");
			return "redirect:/board/listPage";
		}
		
		
		  @RequestMapping(value = "/listCri", method = RequestMethod.GET)
		  public void listAll(Criteria cri, Model model) throws Exception {

		    logger.info("show list Page with Criteria......................");

		    model.addAttribute("list", service.listCriteria(cri));
		  }
		  
		  @RequestMapping(value = "/listPage", method = RequestMethod.GET)
		  public void listPage(@ModelAttribute("cri") Criteria cri, Model model) throws Exception {

		    logger.info(cri.toString());

		    model.addAttribute("list", service.listCriteria(cri));
		    PageMaker pageMaker = new PageMaker();
		    pageMaker.setCri(cri);
//		    pageMaker.setTotalCount(131);

		    pageMaker.setTotalCount(service.listCountCriteria(cri));

		    model.addAttribute("pageMaker", pageMaker);
		  }
		  
//		  @RequestMapping(value = "/readPage", method = RequestMethod.GET)
//		  public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {
//
//		    model.addAttribute(service.read(bno));
//		  }

		  @RequestMapping(value = "/readPage", method = RequestMethod.GET)
		  public void read(@RequestParam("bno") int bno,@ModelAttribute("cri") Criteria cri,Model model) throws Exception {
//			  public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception {
//		    service.remove(bno);
//
//		    rttr.addAttribute("page", cri.getPage());
//		    rttr.addAttribute("perPageNum", cri.getPerPageNum());
//		    rttr.addFlashAttribute("msg", "success");
//
//		    return "redirect:/board/listPage";
			model.addAttribute(service.read(bno));
		  }

}
