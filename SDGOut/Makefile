
# Set the compiler
#
# Note: it is assumed the environment is set up to run gcc. If this is
# not the case, override the variable CC with a valid compiler that uses
# the same command line as gcc
# eg. "make tutorial CC=some_other_compiler".

CC = gcc

# Set the location of the CodeSurfer installation
CSURF_INST = ../../../


# Set the location of the C API include files and codesurfer.lib
# (codesurfer.lib is unnecessary when compiling with gcc).
H_INCLUDE = $(CSURF_INST)include

# Set the csurf executable.  If the path to GrammaTech/CodeSurfer/bin
# (or wherever you installed CodeSurfer) isn't in your path, you can
# override the CSURF variable with an absolute path to the csurf
# executable.
CSURF = csurf
CSURF_COMPILER = $(CC)

# The CodeSurfer project.
TUTORIAL_PROJECT = tutorial_project
TUTORIAL_INCLUDES = tutorial_add.c tutorial_subtract.c tutorial_multiply.c \
			tutorial_divide.c

# Set the tutorial directory
TUTORIAL_DIR := `pwd`
SOSUFFIX=so
ifeq ($(OS),Windows_NT)
TUTORIAL_DIR_WIN := $(shell cygpath -w $(TUTORIAL_DIR) | \
	/bin/sed -e 's@\\@\\\\@g')
SOSUFFIX=dll
LDFLAGS=-mno-cygwin -static-libgcc -L$(CSURF_INST)lib -lcodesurfer
else
TUTORIAL_DIR_WIN := $(TUTORIAL_DIR)
endif


all: tutorial

stk-load-file:
	@rm -f tutorial_example_load.stk
	@echo "(set! *load-path* (cons \"$(TUTORIAL_DIR_WIN)\" *load-path*))" > \
		tutorial_example_load.stk
	@echo "(load \"libtutorial_example.$(SOSUFFIX)\")" >> \
		tutorial_example_load.stk
	@echo "(quit)" >> tutorial_example_load.stk

build-csurf-project:
	@rm -rf CSURF.FILES
	$(CSURF) $(CSURF_FLAGS) -T"$(TUTORIAL_DIR)/CSURF.FILES" \
		-C"$(TUTORIAL_DIR)/CSURF.FILES" $(CSURF_COMPILER) $(CSURF_CFLAGS) \
		-o $(TUTORIAL_PROJECT) $(TUTORIAL_PROJECT).c $(TUTORIAL_INCLUDES)

build-so:
	$(CC) -shared -I$(H_INCLUDE) \
		tutorial_example.c -o libtutorial_example.$(SOSUFFIX) $(LDFLAGS) -fPIC

tutorial: FORCE  build-so stk-load-file
	$(CSURF) -nogui $(TUTORIAL_PROJECT) \
		-l "$(TUTORIAL_DIR)/tutorial_example_load.stk"

clean:
	rm -f $(TUTORIAL_PROJECT).prj
	rm -f $(TUTORIAL_PROJECT).exe
	rm -rf CSURF.FILES
	rm -f *.o *.obj
	rm -f libtutorial_example.dll
	rm -f libtutorial_example.so
	rm -f libtutorial_example.lib
	rm -f tutorial_example.obj
	rm -f libtutorial_example.exp
	rm -f tutorial_example_load.stk

.PHONY: FORCE

FORCE: 

