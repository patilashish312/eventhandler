package com.cs.assignment.eventhandler.service;

import static org.mockito.Mockito.times;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.io.ClassPathResource;
import com.cs.assignment.eventhandler.repository.DBEventRepository;



@ExtendWith(MockitoExtension.class)
public class LogEventHandlerServiceTest {

	@Mock
	private DBEventRepository dbEventRepository;

	@InjectMocks
	private LogEventHandlerService eventHandlerService;

	
	@ParameterizedTest
	@MethodSource("provider")
	@DisplayName("Interaction verification")
	public void verify_interaction(String fileName,int numberTimes) {
		try {
			URI uri = new ClassPathResource(fileName).getURI();
			Path path = Paths.get(uri);
			eventHandlerService.logFileProcessor(path);
			Mockito.verify(dbEventRepository, times(numberTimes)).save(Mockito.any());
		} catch (Exception e) {			
			Assertions.fail("Exception thrown, test failed"+e);
		}
	}
	
	private static Stream<Arguments> provider(){
		return Stream.of(Arguments.of("testlogfile.txt",4),Arguments.of("testlogfileinvalid.txt",3));
	}
	
	@ExtendWith(OutputCaptureExtension.class)
	@Test
	@DisplayName("Logger verification")
	public void verify_logs(CapturedOutput capturedOutput) {
		URI uri;
		try {
			uri = new ClassPathResource("testlogfile.txt").getURI();
			Path path = Paths.get(uri);
			eventHandlerService.logFileProcessor(path);
			capturedOutput.getAll().contains("Displaying all saved entities");
			capturedOutput.getAll().contains("DBEvent(id=scsmbstgra, duration=5, type=APPLICATION_LOG, host=12345, alert=true)");
			capturedOutput.getAll().contains("DBEvent(id=scsmbstgrb, duration=3, type=null, host=null, alert=false)");
			capturedOutput.getAll().contains("DBEvent(id=scsmbstgrc, duration=8, type=null, host=null, alert=true)");
			capturedOutput.getAll().contains("DBEvent(id=scsmbstgrd, duration=1, type=null, host=null, alert=false)");	
		} catch (IOException e) {
			Assertions.fail("Exception thrown, test failed"+e);
		}
		
	}
	

}
