package com.tigeorgia.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/logging")
public class LogController {
	
	private static final Logger logger = Logger.getLogger(LogController.class);
	
	private static final String MESSAGE_TAG = "SENTMESSAGE";
	
	@RequestMapping(method = RequestMethod.GET)
	public String displayLogFile(ModelMap model) {
		logger.info("opening log file now...");
		List<String> messageLog = new LinkedList<String>();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("/tmp/electionsms/logfile.log");
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			while (br.ready()){
				String line = br.readLine();
				if (line != null && !line.isEmpty()){
					if (line.contains(MESSAGE_TAG)){
						String resultLine = line.replaceAll(MESSAGE_TAG, "");
						messageLog.add(resultLine);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// Log file doesn't exist.
			String errorMessage = "log file does not exist!";
			logger.error(errorMessage, e);
			model.addAttribute("errorMsg", "ERROR: " + errorMessage);
		} catch (IOException e) {
			String errorMessage = "line in log file could not be read.";
			logger.error(errorMessage, e);
			model.addAttribute("errorMsg", "ERROR: " + errorMessage);
		}finally{

			// releases system resources associated with this stream
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		model.addAttribute("messageLog",messageLog);
		
		return "logfile";
 
	}

}
