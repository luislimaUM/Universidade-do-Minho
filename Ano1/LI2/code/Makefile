CFLAGS=-Wall -Wextra -ansi -pedantic -O2
OBJS=$(patsubst %.c,%.o,$(wildcard *.c))


bn: $(OBJS)
	$(CC) $(CFLAGS) -o bn $(OBJS)

clean:
	rm bn *.o