.SUFFIXES: .cpp .o .c

CC = g++  -fpermissive 
CCFLAG =  -c -g -fPIC -Wall -D_LITTLEENDIAN -D_LINUX
MK_SO = g++ -fpermissive -pthread -fPIC -shared

INC=-I. -I$(D5000_HOME)/source/include -I$(D5000_HOME)/source/include/msg_api

.c.o .cpp.o:
	@echo	$(CC)  $(CCFLAG) $(INC) $<
	@	$(CC)  $(CCFLAG) $(INC) $<

all: libjrte.so

libjrte.so: MessageJNI.o
	@echo	$(MK_SO) -o libjrte.so MessageJNI.o -L$(D5000_HOME)/lib -lrte
	@	$(MK_SO) -o libjrte.so MessageJNI.o -L$(D5000_HOME)/lib -lrte

clean:
	rm -f *.o
