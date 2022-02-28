# eventhandler

- Task Summary:-

	Parse & process event logs & insert it to HSQLDB.

- Specifications:-


	1. Java8		
	2. Spring boot 2.5.0	
	3. Maven 4	
	4. HSQLDB 2.2.8
	

	
- How to run code:- 

	1.Make sure HSQLDB is up & running locally with following command : 
	  `../org/hsqldb/hsqldb/2.2.8/hsqldb-2.2.8.jar org.hsqldb.Server --database.0 file:data -dbname.0 testdb`
	  
	2. Go to project & start application with below command:
	  `mvn clean install spring-boot:run`
	  
	3. Based on logger requirement we can change log to debug or info based on application property config.
	
	4. Once application is successfully executed, you can see entries being printed on console something like below:
	
		2022-02-28 11:42:18 [main] INFO  c.c.a.e.s.LogEventHandlerService - Displaying all saved entities
		2022-02-28 11:42:19 [main] INFO  c.c.a.e.s.LogEventHandlerService - DBEvent(id=scsmbstgra, duration=5,type=APPLICATION_LOG, host=12345, alert=true)
		2022-02-28 11:42:19 [main] INFO  c.c.a.e.s.LogEventHandlerService - DBEvent(id=scsmbstgrb, duration=3,type=null, 	host=null, alert=false)
		2022-02-28 11:42:19 [main] INFO  c.c.a.e.s.LogEventHandlerService - DBEvent(id=scsmbstgrc, duration=8,type=null, 	host=null, alert=true)
		2022-02-28 11:42:19 [main] INFO  c.c.a.e.s.LogEventHandlerService - DBEvent(id=scsmbstgrd, duration=1,type=null, 	host=null, alert=false)
	

- Notes:-
	
	 1. If event has same status e.g. FINISHED, then that is not being checked & code will still calculate time difference & insert int dbname
	 2. IF there is single event & no associate event present, then that is being ignored.
	 3. For time-being,each time code executes, file table got created.
	 4. Currently code assumes input is well formed json & don't have nulls for `id`, `timestamp`.
	 5. Currently code assumes that every event has _only_ 2 entries in logfile.txt i.e. if there are 3 entries then one of entry will get ignored; if 
      	    there are 4 entries then execution failed with constraint violation error because here we are trying to save another event with same id twice.
            Error observed in this case is : 
	    `2022-02-28 12:12:42 [ForkJoinPool.commonPool-worker-3] WARN  o.h.e.jdbc.spi.SqlExceptionHelper - SQL Error: -104, SQLState: 23505
	     2022-02-28 12:12:42 [ForkJoinPool.commonPool-worker-3] ERROR o.h.e.jdbc.spi.SqlExceptionHelper - integrity constraint violation: unique constraint or index 			violation; SYS_PK_10533 table: DBEVENT`


