package com.cs.assignment.eventhandler.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ServerEvent {

	String id;
	
	String state;
	
	String type;
	
	String host;
	
	long timestamp;
}
