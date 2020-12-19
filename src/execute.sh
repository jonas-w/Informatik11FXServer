javac -classpath ./ net/jonasw/informatikserver/Main.java
java net/jonasw/informatikserver/Main
find ./ | grep .class | xargs rm -f
