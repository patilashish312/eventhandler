package com.cs.assignment.eventhandler.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.assignment.eventhandler.io.DBEvent;
import com.cs.assignment.eventhandler.io.ServerEvent;
import com.cs.assignment.eventhandler.repository.DBEventRepository;
import com.google.gson.Gson;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class LogEventHandlerService {

	@Autowired
	DBEventRepository dbEventRepository;

	public void logFileProcessor(Path path) {
		Gson gson = new Gson();
		Stream<String> events = null;
		ConcurrentHashMap<String, ServerEvent> map = new ConcurrentHashMap<String, ServerEvent>();
		try {
			events = Files.lines(path);

			events.parallel().forEach(x -> {
				ServerEvent serverEvent = gson.fromJson(x, ServerEvent.class);
				String id = serverEvent.getId();
				long duration = 0;
				boolean alert = false;

				if (null == map.putIfAbsent(id, serverEvent)) {
					return;
				}

				log.debug("Removing entry from map with key : " + id);
				ServerEvent prevEvent = map.remove(id);
				duration = Math.abs(serverEvent.getTimestamp() - prevEvent.getTimestamp());
				alert = false;
				if (duration > 4) {
					alert = true;
				}
				log.debug("Saving entity to db for key : " + id);
				DBEvent savedEvent = dbEventRepository.save(DBEvent.builder().id(id).duration(duration)
						.type(serverEvent.getType()).host(serverEvent.getHost()).alert(alert).build());
				log.debug("Save success for entity :" + savedEvent);

			});
			events.close();

			log.info("Displaying all saved entities");
			dbEventRepository.findAll().forEach(x -> {
				log.info(x);
			});

		} catch (Exception e) {
			log.error("Error while processing requests:" + e);
		}
	}

}
