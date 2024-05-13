package com.sist.nbgb.dto;

import java.time.LocalDateTime;

import com.sist.nbgb.entity.Instructors;
import com.sist.nbgb.entity.OnlineCategory;


public interface OnlinePaymentClassListDTO {
	String getPartnerOrderId();
	String getPartnerUserId();
	String getItemCode();
	LocalDateTime getApprovedAt();
	
	Long getOnlineClassId();
	String getOnlineClassTitle();
	Instructors getInstructorId();
	Long getOnlineClassPeriod();
	
}
