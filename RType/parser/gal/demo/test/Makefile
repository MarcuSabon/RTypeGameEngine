JAVAPROJECT = ../../../..
CLASSPATH = $(JAVAPROJECT)/bin
PARSER = gal.parser.Parser

GAL_FILES = $(wildcard *.gal)

help:
	@echo "1. INSTALL JavaCC with command: apt get install javacc"
	@echo "2. RUN JavaCC + JavaCompiler with command: make parser"
	@echo "3. PARSE file.gal with command: make file.txt, make file.aut.jpg, make file.ast.jpg"  	

parser:
	@make -C $(JAVAPROJECT)/parser/gal/parser/

test0: a02.ast.jpg a02.aut.jpg

test1: GAL2025.ast.jpg GAL2025.aut.jpg

demo: $(GAL_FILES:%.gal=%.ast.jpg) $(GAL_FILES:%.gal=%.aut.jpg)

%.txt: %.gal 
	@java -cp $(CLASSPATH) $(PARSER) -file $< -src

%.aut.dot: %.gal 
	@java -cp $(CLASSPATH) $(PARSER) -file $< -aut > $@

%.ast.dot: %.gal 
	@java -cp $(CLASSPATH) $(PARSER) -file $< -ast > $@

%.jpg: %.dot
	@dot -Tjpg $< > $@
	@open $@
	
clean:
	-@rm -f *.jpg *.dot