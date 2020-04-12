all : clean Client.java
	javac Client.java

exec : Client.class
	java Client 127.0.0.1 1337 Archspire


clean :
	rm -f *.class
