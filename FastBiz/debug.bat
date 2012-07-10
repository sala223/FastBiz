ECHO ON
SET JAVA=java
SET CWD=%~dp0
PUSHD %CWD%
SET DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8888
SET MEM_OPTS=-Xmx1024m -Xms512m 
SET JAVA_OPTS=%DEBUG_OPTS%, %MEM_OPTS% -XX:+TraceClassLoading -XX:+UseParallelGC -XX:ParallelGCThreads=2
%JAVA% %JAVA_OPTS% -cp %CWD%/bootstrap com.miniebs.main.Main
POPD  