ECHO OFF
SET JAVA=java
SET CWD=%~dp0
PUSHD %CWD%
SET DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8888
SET MEM_OPTS=-Xmx1024m -Xms512m 
SET JAVA_OPTS=%DEBUG_OPTS%, %MEM_OPTS% -XX:+UseParallelGC -XX:ParallelGCThreads=2
%JAVA% %JAVA_OPTS% -cp %CWD%/boot com.fastbiz.boot.main.Main
POPD  