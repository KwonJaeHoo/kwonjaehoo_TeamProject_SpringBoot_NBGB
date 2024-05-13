package com.sist.nbgb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sist.nbgb.dto.InstructorOfflineLecture;
import com.sist.nbgb.dto.OnlineClassView;
import com.sist.nbgb.dto.OnlineInsDto;
import com.sist.nbgb.entity.OfflineClass;
import com.sist.nbgb.enums.Status;
import com.sist.nbgb.response.OfflineResponse;
import com.sist.nbgb.response.OfflineReviewResponse;
import com.sist.nbgb.response.UserResponse;
import com.sist.nbgb.service.InstructorsService;
import com.sist.nbgb.service.OfflineInstructorService;
import com.sist.nbgb.service.OfflineService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OfflineInstructorController 
{
	private final InstructorsService instructorsService;
	private final OfflineInstructorService offlineInstructorService;
	private final OfflineService offlineService;
	
	@GetMapping("/instructor/mypage/{id}/offlinelecture")
	public String offlinelecture(Model model, @PathVariable String id, @RequestParam(value="page", defaultValue="0") int page)
	{
		List<OfflineResponse> list = offlineInstructorService.findByInstructorIdDesc(id).stream()
				.map(OfflineResponse::new)
				.collect(Collectors.toList());
		
		Page<OfflineClass> paging = this.offlineInstructorService.findByInstructorId(page, id);
		Page<OfflineResponse> toMap = paging.map(m -> new OfflineResponse(m));
		List<OfflineResponse> listPaging = toMap.getContent();
		
		Status N = Status.N;
		
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		model.addAttribute("listPaging", listPaging);
		model.addAttribute("N", N);
		
		return "/mypage/instructor/offlinelecture";
	}
	
	@GetMapping("/instructor/mypage/{id}/onlinelecture")
	public String onlinelecture(Model model, @PathVariable String id, @RequestParam(value="page", defaultValue="0") int page)
	{
		List<OnlineInsDto> listPaging = offlineInstructorService.findByInstuctorId(id).stream()
				.map(OnlineInsDto::new)
				.collect(Collectors.toList());
		
		PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "OnlineClassId"));
		int start = (int) pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), listPaging.size());
		Page<OnlineInsDto> paging = new PageImpl<>(listPaging.subList(start, end), pageRequest, listPaging.size());
		
		model.addAttribute("paging", paging);
		model.addAttribute("listPaging", listPaging);
		
		return "/mypage/instructor/onlinelecture";
	}
	
	@GetMapping("/instructor/mypage/{id}/offlinelecturerequest")
	public String offlinelecturerequest(Model model, @PathVariable String id, @RequestParam(value="page", defaultValue="0") int page)
	{	
//		Page<OfflineClass> paging = this.offlineInstructorService.offlinePayInstructor(page, id, Status.Y);
//		Page<OfflineResponse> toMap = paging.map(m -> new OfflineResponse(m));
		List<OfflineResponse> listPaging = offlineInstructorService.offlinePayInstructor(page, id, null).stream()
				.map(OfflineResponse::new)
				.collect(Collectors.toList());
		
		PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "OfflineClassId"));
		int start = (int) pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), listPaging.size());
		Page<OfflineResponse> paging = new PageImpl<>(listPaging.subList(start, end), pageRequest, listPaging.size());
		
		model.addAttribute("paging", paging);
		model.addAttribute("listPaging", listPaging);
		
		return "/mypage/instructor/offlinelecturerequest";
	}
	
	@GetMapping("/instructor/mypage/offlinelecturerequest/{offlineClassId}")
	public String offlinelecturerequestView(Model model, @PathVariable Long offlineClassId)
	{			
		OfflineClass offlineClass = offlineService.findByView(offlineClassId);
		
		model.addAttribute("offlineClass", offlineClass);
		
		return "/mypage/instructor/offlinelecturerequestView";
	}
	
	@PostMapping("/instructor/mypage/offlinelecturerequest/time")
	@ResponseBody //OfflinePaymentApproveDto
	public ResponseEntity<ArrayList<String>> time(@RequestParam("date") String date, @RequestParam("offlineClassId") String offlineClassId)
	{
		ArrayList<String> timeList = new ArrayList<>(offlineInstructorService.timeList(offlineClassId, date));
		
		return ResponseEntity.ok(timeList);
	}
	
	@PostMapping("/instructor/mypage/offlinelecturerequest/selectedList")
	@ResponseBody
	public ResponseEntity<List<UserResponse>> selectedList(@RequestParam("time") String time, @RequestParam("date") String date, @RequestParam("offlineClassId") String offlineClassId)
	{
		List<UserResponse> list = offlineInstructorService.payList(date, time, offlineClassId).stream()
				.map(UserResponse::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/instructor/mypage/offlinelecture/statuschange")
	@ResponseBody
	public ResponseEntity<Object> offlineLectureStatusChange(@RequestBody InstructorOfflineLecture instructorOfflineLecture)
	{
		return ResponseEntity.ok(instructorsService.instructorOfflineLectureStatusChage(instructorOfflineLecture.getOfflineClassId()));
	}
	
	@PostMapping("/instructor/mypage/onlinelecture/statuschange")
	@ResponseBody
	public ResponseEntity<Object> onlineLectureStatusChange(@RequestParam("onlineClassId") Long onlineClassId)
	{
		return ResponseEntity.ok(offlineInstructorService.instructorOnlineLectureStatusChage(onlineClassId));
	}
	
	@GetMapping("/instructor/mypage/{id}/review")
	public String review(Model model, @PathVariable String id, @RequestParam(value="page", defaultValue="0") int page)
	{	
//		Page<OfflineClass> paging = this.offlineInstructorService.offlinePayInstructor(page, id, Status.Y);
//		Page<OfflineResponse> toMap = paging.map(m -> new OfflineResponse(m));
		List<OfflineReviewResponse> listPaging = offlineInstructorService.insReview(id).stream()
				.map(OfflineReviewResponse::new)
				.collect(Collectors.toList());
		
		PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "ReviewId"));
		int start = (int) pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), listPaging.size());
		Page<OfflineReviewResponse> paging = new PageImpl<>(listPaging.subList(start, end), pageRequest, listPaging.size());
		
		model.addAttribute("paging", paging);
		model.addAttribute("listPaging", listPaging);
		
		return "/mypage/instructor/instructorReview";
	}
	
}
