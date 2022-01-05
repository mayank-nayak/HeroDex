default: Compile.class
	java HeroDex

test: Compile.class
	java -jar junit5.jar -cp . --scan-classpath -n HeroDex

Compile.class: HeroDex.java Recent.class
	javac -cp .:junit5.jar HeroDex.java -Xlint

Recent.class: Hero.java Recent2.class
	javac Hero.java

Recent2.class: HashTableMap.java Recent3.class
	javac HashTableMap.java

Recent3.class: MapADT.java
	javac MapADT.java

clean:
	rm *.class
