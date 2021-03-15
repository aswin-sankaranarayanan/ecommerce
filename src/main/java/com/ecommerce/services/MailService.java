package com.ecommerce.services;

import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.User;
import com.ecommerce.repository.OrderProjection;

@Service
public class MailService {

	@Value("${mail.salutation}")
	private String salutation;
	
	@Value("${mail.startline}")
	private String startline;
	
	@Value("${mail.endline}")
	private String endline;
	
	
	@Value("${mail.subject}")
	private String subject;
	
	@Value("${admin.mail}")
	private String adminMailId;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(User user, Long orderId) {
		StringBuffer buffer = new StringBuffer();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		String fullName= user.getFirstName()+" "+user.getLastName();
		try {
			helper.setTo(user.getEmailId());
			helper.setBcc(adminMailId);
			helper.setSubject(subject);
			buffer.append(salutation.replace(":name", fullName));
			buffer.append(startline.replace(":orderId", String.valueOf(orderId)));
			buffer.append("Please find the Order");
			buffer.append("<ul>");
			List<OrderProjection> orderDetails = orderService.getOrderDetails(orderId);
			for (OrderProjection orderDetail : orderDetails) {
				buffer.append("<li>");
				buffer.append(orderDetail.getItem());
				buffer.append("</li>");
			}
			buffer.append("</ul>");
			buffer.append(endline);
			
			String emailText = buffer.toString();
			helper.setText(emailText, true);
			System.out.println(emailText);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
}
