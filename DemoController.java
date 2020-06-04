package com.example.demo;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.vo.Vo;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Controller
public class DemoController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/upload")
	public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

		System.out.println("Hi");
		// validate file
		if (file.isEmpty()) {
			model.addAttribute("message", "Please select a CSV file to upload.");
			model.addAttribute("status", false);
		} else {

			// parse CSV file to create a list of `User` objects
			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

				// create csv bean reader
				CsvToBean<GCPCalVo> csvToBean = new CsvToBeanBuilder(reader)
						.withType(GCPCalVo.class)
						.withIgnoreLeadingWhiteSpace(true)
						.build();

				// convert `CsvToBean` object to list of users
				List<GCPCalVo> users = csvToBean.parse();
				
				//String query = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalram from gcp_cal where serviceRegions0 =? and categoryresourceFamily = 'Compute' and categoryusageType =? and categoryresourceGroup =? union select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =? and categoryresourceFamily = 'Compute' and categoryusageType =? and categoryresourceGroup =?";
			String query1 = "	select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalram from gcp where serviceRegions0 =?  and categoryresourceGroup ='RAM' ";
			String query2 = "	select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalram from gcp where serviceRegions0 =?  and categoryresourceGroup ='CPU' ";
				for(GCPCalVo vo : users) {
					String commitmentType = users.get(0).getCommitmentType();
					String memory = users.get(0).getMemory();
					String location = users.get(0).getLocation();
					String vCpu = users.get(0).getvCPU();
					String oS = users.get(0).getOperatingSystem();
					String instanceType = users.get(0).getInstanceType();
					
					String commitmentType2 = users.get(0).getCommitmentType();
					String memory2 = users.get(0).getMemory();
					String location2 = users.get(0).getLocation();
					String vCpu2 = users.get(0).getvCPU();
					String oS2 = users.get(0).getOperatingSystem();
					String instanceType2 = users.get(0).getInstanceType();
					
					jdbcTemplate.queryForObject(query1,new Object[] {location},new
							RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
									int rowNum) throws SQLException { Vo obj = new
									Vo();
							String value = rs.getString("totalram");
							System.out.println("Value1: "+value);

							
							return obj; 
					}
			} );
					
					
					jdbcTemplate.queryForObject(query2,new Object[] {location},new
							RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
									int rowNum) throws SQLException { Vo obj = new
									Vo();
							String value = rs.getString("totalram");
							System.out.println("Value2: "+value);

							
							return obj; 
					}
			} );
				
				}
				
				// TODO: save users in DB?
				/*
				 * users.get(0).getCommitmentType();
				 * 
				 * System.out.println("Commitment Type "+users.get(0).getCommitmentType());
				 * System.out.println("Commitment Type "+users.get(0).getMemory());
				 * System.out.println("Commitment Type "+users.get(0).getLocation());
				 * System.out.println("Commitment Type "+users.get(0).getvCPU());
				 * System.out.println("Commitment Type "+users.get(0).getOperatingSystem());
				 * System.out.println("Commitment Type "+users.get(0).getInstanceType());
				 */
				
				
				
				//select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalram from gcp_cal where serviceRegions0 ='us-central1' and categoryresourceFamily = 'compute' and categoryusageType ="Commit1Yr" and categoryresourceGroup ='RAM'
				//union 
				//select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 ='us-central1' and categoryresourceFamily = 'compute' and categoryusageType ="Commit1Yr" and categoryresourceGroup ='CPU'

				// save users list on model
				model.addAttribute("users", users);
				//model.addAttribute("users", );
				
				model.addAttribute("status", true);

			} catch (Exception ex) {
				model.addAttribute("message", "An error occurred while processing the CSV file.");
				model.addAttribute("status", false);
			}
		}

		return "file-upload-status";
	}
	
	@PostMapping("/abc")
	public String saveNewLaunchPlan(@RequestBody Map<String,String> vo){
		return "Shubhm";
	}
	
}