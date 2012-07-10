ECHO OFF
SET JAVA=java
SET CWD=%~dp0
PUSHD %CWD%
java -cp %CWD%/runtime/bootstrap com.miniebs.main.Main
POPD  