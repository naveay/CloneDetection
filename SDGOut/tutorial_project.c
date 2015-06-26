#include "tutorial_project.h"
int printf(const char *fmt, ...);

int main()
{
  int a, b, c;
  a = 1;
  b = 3;
  
  c = t_add(a, b);
  a = t_subtract(b, a);
  b = t_divide(c, a);
  c = t_multiply(a, b);

  printf("Hello, World!  I have used addition, subtraction, multiplication, and division.\n");

  return 0;
}
